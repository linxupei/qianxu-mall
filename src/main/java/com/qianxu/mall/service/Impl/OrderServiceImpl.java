package com.qianxu.mall.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.qianxu.mall.common.Constant;
import com.qianxu.mall.exception.QianxuMallException;
import com.qianxu.mall.exception.QianxuMallExceptionEnum;
import com.qianxu.mall.filter.UserFilter;
import com.qianxu.mall.model.dao.CartMapper;
import com.qianxu.mall.model.dao.OrderItemMapper;
import com.qianxu.mall.model.dao.OrderMapper;
import com.qianxu.mall.model.dao.ProductMapper;
import com.qianxu.mall.model.pojo.Order;
import com.qianxu.mall.model.pojo.OrderItem;
import com.qianxu.mall.model.pojo.Product;
import com.qianxu.mall.model.request.CreateOrderReq;
import com.qianxu.mall.model.vo.CartVO;
import com.qianxu.mall.model.vo.OrderItemVO;
import com.qianxu.mall.model.vo.OrderVO;
import com.qianxu.mall.service.CartService;
import com.qianxu.mall.service.OrderService;
import com.qianxu.mall.service.UserService;
import com.qianxu.mall.util.OrderCodeFactory;
import com.qianxu.mall.util.QRCodeGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 谦虚
 * @version 1.0
 * @date 2021/3/13 10:41
 * @describe 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartService cartService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Value("${file.upload.ip}")
    private String ip;
    @Autowired
    private UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq createOrderReq) {
        //拿到用户id
        //从购物车中找到已经勾选的商品
        //如果购物车为空, 报错
        Integer userId = UserFilter.currentUser.getId();
        List<CartVO> cartVOS = cartService.list(userId).stream()
                .filter(cartVO -> cartVO.getSelected().equals(Constant.CheckStatus.CHECKED))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cartVOS)) {
            throw new QianxuMallException(QianxuMallExceptionEnum.CART_EMPTY);
        }

        //判断商品是否存在, 上下架状态, 库存
        validSaleStatusAndStock(cartVOS);
        //把购物车对象转为订单item对象
        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOS);

        //扣库存
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            int stock = product.getStock() - orderItem.getQuantity();
            if (stock < 0) {
                throw new QianxuMallException(QianxuMallExceptionEnum.NOT_ENOUGH);
            }
            product.setStock(stock);
            productMapper.updateByPrimaryKeySelective(product);
        }

        //把购物车中的已勾选商品删除
        cleanCart(cartVOS);

        //生成订单
        //生成订单号, 有独立的规则
        Order order = new Order();
        String orderNo = OrderCodeFactory.getOrderCode(userId.longValue());
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(orderItemList.stream().mapToInt(OrderItem::getTotalPrice).sum());
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());
        order.setPostage(0);
        order.setPaymentType(1);

        //插入到Order表
        orderMapper.insertSelective(order);
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(orderNo);
            orderItemMapper.insertSelective(orderItem);
        }

        return orderNo;
    }



    private void cleanCart(List<CartVO> cartVOS) {
        for (CartVO cartVO : cartVOS) {
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }
    }

    private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOS) {
        List<OrderItem> orderItemList = new LinkedList<>();
        for (CartVO cartVO : cartVOS) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private void validSaleStatusAndStock(List<CartVO> cartVOS) {
        for (CartVO cartVO : cartVOS) {
            //判断商品是否存在, 商品是否上架
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            if (product == null || !product.getStatus().equals(Constant.SaleStatus.SALE)) {
                throw new QianxuMallException(QianxuMallExceptionEnum.NOT_SALE);
            }
            //判断商品库存
            if (cartVO.getQuantity() > product.getStock()) {
                throw new QianxuMallException(QianxuMallExceptionEnum.NOT_ENOUGH);
            }
        }
    }

    @Override
    public OrderVO detail(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //判断订单是否存在
        if (order == null) {
            throw new QianxuMallException(QianxuMallExceptionEnum.NO_ORDER);
        }
        Integer userId = UserFilter.currentUser.getId();
        //判断订单所属
        if (!order.getUserId().equals(userId)) {
            throw new QianxuMallException(QianxuMallExceptionEnum.NOT_YOUR_ORDER);
        }
        OrderVO orderVO = getOrderVO(order);
        return orderVO;
    }

    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList = new LinkedList<>();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        return orderVO;
    }

    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Integer userId = UserFilter.currentUser.getId();
        List<Order> orderList = orderMapper.selectForCustomer(userId);
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo pageInfo = new PageInfo(orderVOList);
        return pageInfo;
    }

    private List<OrderVO> orderListToOrderVOList(List<Order> orderList) {
        List<OrderVO> orderVOList = new LinkedList<>();
        for (Order order : orderList) {
            OrderVO orderVO = getOrderVO(order);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    @Override
    public void cancel(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //判断订单是否存在
        if (order == null) {
            throw new QianxuMallException(QianxuMallExceptionEnum.NO_ORDER);
        }
        Integer userId = UserFilter.currentUser.getId();
        //判断订单所属
        if (!order.getUserId().equals(userId)) {
            throw new QianxuMallException(QianxuMallExceptionEnum.NOT_YOUR_ORDER);
        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new QianxuMallException(QianxuMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public String qrcode(String orderNo) {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //通过这个方法可以通过连接局域网, 用手机扫码
//        try {
//            ip = InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
        String address = ip + ":" + request.getLocalPort();
        String payUrl = "http://" + address + "/pay?orderNo=" + orderNo;
        try {
            QRCodeGenerator.generateQRCodeImage(payUrl, 350, 350,
                    Constant.FILE_UPLOAD_DIR + orderNo + ".png");
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pagAddress = "http://" + address + "/images/" + orderNo + ".png";
        return pagAddress;
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectForAdmin();
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo pageInfo = new PageInfo(orderVOList);
        return pageInfo;
    }

    @Override
    public void pay(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单, 报错
        if (order == null) {
            throw new QianxuMallException(QianxuMallExceptionEnum.NO_ORDER);
        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.PAID.getCode());
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new QianxuMallException(QianxuMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public void delivered(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单, 报错
        if (order == null) {
            throw new QianxuMallException(QianxuMallExceptionEnum.NO_ORDER);
        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.PAID.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.DELIVERED.getCode());
            order.setDeliveryTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new QianxuMallException(QianxuMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public void finish(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单, 报错
        if (order == null) {
            throw new QianxuMallException(QianxuMallExceptionEnum.NO_ORDER);
        }
        //如果是普通用户, 就要校验订单所属
        if (!userService.checkAdminRole(UserFilter.currentUser) &&
                !order.getUserId().equals(UserFilter.currentUser.getId())) {
            throw new QianxuMallException(QianxuMallExceptionEnum.NOT_YOUR_ORDER);
        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.DELIVERED.getCode())) {
            order.setOrderStatus(Constant.OrderStatusEnum.FINISHED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            throw new QianxuMallException(QianxuMallExceptionEnum.WRONG_ORDER_STATUS);
        }
    }
}
