package com.spring.elastic.module.mongo.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.mongoplus.annotation.transactional.MongoTransactional;
import com.mongoplus.model.PageParam;
import com.mongoplus.model.PageResult;
import com.mongoplus.service.impl.ServiceImpl;
import com.spring.elastic.common.enums.business.BaseEnum;
import com.spring.elastic.common.exception.BusinessException;
import com.spring.elastic.module.mongo.dto.UserDTO;
import com.spring.elastic.module.mongo.dto.UserPageQuery;
import com.spring.elastic.module.mongo.dto.UserStatusReq;
import com.spring.elastic.module.mongo.entity.User;
import com.spring.elastic.module.mongo.enums.business.UserStatusEnum;
import com.spring.elastic.module.mongo.enums.response.UserResponseEnum;
import com.spring.elastic.module.mongo.service.IUserService;
import com.spring.elastic.module.mongo.vo.UserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户服务实现类
 *
 * @author qujianlei
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<User> implements IUserService {

    private final ModelMapper modelMapper;

    @Override
    @MongoTransactional(rollbackFor = Exception.class)
    public boolean addUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        // 校验用户名称不能重复
        long count = this.lambdaQuery()
                .eq(User::getName, userDTO.getName())
                .count();
        if (count > 0) {
            throw new BusinessException(UserResponseEnum.USER_NAME_EXIST,
                String.format(UserResponseEnum.USER_NAME_EXIST.getMessage(), userDTO.getName()));
        }

        // 校验用户邮箱不能重复
        count = this.lambdaQuery()
                .eq(User::getEmail, userDTO.getEmail())
                .count();
        if (count > 0) {
            throw new BusinessException(UserResponseEnum.USER_EMAIL_EXIST,
                String.format(UserResponseEnum.USER_EMAIL_EXIST.getMessage(), userDTO.getEmail()));
        }

        // 强制初始化逻辑
        user.setBalance(BigDecimal.ZERO); // 初始余额为0
        user.setStatus(UserStatusEnum.NORMAL.getCode()); // 初始状态为正常
        
        return this.save(user);
    }

    @Override
    @MongoTransactional(rollbackFor = Exception.class)
    public boolean updateUser(UserDTO userDTO) {
        User user = this.getById(userDTO.getId());
        if (user == null) {
            throw new BusinessException(UserResponseEnum.USER_NOT_EXIST,
                    String.format(UserResponseEnum.USER_NOT_EXIST.getMessage(), userDTO.getId()));
        }

        // 校验用户名称不能重复
        long count = this.lambdaQuery()
                .eq(User::getName, userDTO.getName())
                .ne(User::getId, userDTO.getId())
                .count();
        if (count > 0) {
            throw new BusinessException(UserResponseEnum.USER_NAME_EXIST,
                String.format(UserResponseEnum.USER_NAME_EXIST.getMessage(), userDTO.getName()));
        }
        
        // 校验用户邮箱不能重复
        count = this.lambdaQuery()
                .eq(User::getEmail, userDTO.getEmail())
                .ne(User::getId, userDTO.getId())
                .count();
        if (count > 0) {
            throw new BusinessException(UserResponseEnum.USER_EMAIL_EXIST,
                String.format(UserResponseEnum.USER_EMAIL_EXIST.getMessage(), userDTO.getEmail()));
        }
        
        // 只更新允许修改的字段
        modelMapper.map(userDTO, user);
        
        // 注意：这里显式忽略了 balance 和 status 的更新，确保安全
        
        return this.updateById(user);
    }

    @Override
    @MongoTransactional(rollbackFor = Exception.class)
    public boolean updateStatus(UserStatusReq req) {
        // 校验状态值是否有效
        boolean isValidStatus = BaseEnum.isValidCode(UserStatusEnum.class, req.getStatus());
        if (!isValidStatus) {
            throw new BusinessException(UserResponseEnum.USER_STATUS_INVALID, 
                String.format(UserResponseEnum.USER_STATUS_INVALID.getMessage(), req.getStatus()));
        }

        User user = this.getById(req.getId());
        if (user == null) {
            throw new BusinessException(UserResponseEnum.USER_NOT_EXIST,
                String.format(UserResponseEnum.USER_NOT_EXIST.getMessage(), req.getId()));
        }

        user.setStatus(req.getStatus());
        return this.updateById(user);
    }

    @Override
    @MongoTransactional(rollbackFor = Exception.class)
    public boolean deleteUser(String id) {
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(UserResponseEnum.USER_NOT_EXIST,
                String.format(UserResponseEnum.USER_NOT_EXIST.getMessage(), id));
        }
        return this.removeById(id);
    }

    @Override
    public UserVO getUser(String id) {
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(UserResponseEnum.USER_NOT_EXIST,
                String.format(UserResponseEnum.USER_NOT_EXIST.getMessage(), id));
        }
        UserVO userVO = modelMapper.map(user, UserVO.class);
        // 转换状态描述
        String statusStr = BaseEnum.getByCode(UserStatusEnum.class, user.getStatus()).getDesc();
        userVO.setStatusStr(statusStr);

        return userVO;
    }

    @Override
    public PageResult<UserVO> pageList(UserPageQuery query) {
        // 1. 构建分页对象
        PageParam pageParam = new PageParam(query.getPage(), query.getSize());
        
        // 2. 执行查询并转换 (PO -> VO)
        PageResult<User> userPage = this.lambdaQuery()
                .like(StringUtils.isNotBlank(query.getName()), User::getName, query.getName())
                .like(StringUtils.isNotBlank(query.getEmail()), User::getEmail, query.getEmail())
                .eq(query.getStatus() != null, User::getStatus, query.getStatus())
                .orderByDesc(User::getCreatedAt)
                .page(pageParam);
        
        // 3. 转换状态描述
        List<UserVO> userVOList = userPage.getContentData().stream()
                .map(user -> {
                    UserVO userVO = modelMapper.map(user, UserVO.class);
                    String statusDesc = BaseEnum.getByCode(UserStatusEnum.class, user.getStatus()).getDesc();
                    userVO.setStatusStr(statusDesc);
                    return userVO;
                })
                .collect(Collectors.toList());

        PageResult<UserVO> result = new PageResult<>(userPage.getPageNum(), userPage.getPageSize(),
            userPage.getTotalSize(), userPage.getTotalPages(), userVOList);

        return result;
    }

}