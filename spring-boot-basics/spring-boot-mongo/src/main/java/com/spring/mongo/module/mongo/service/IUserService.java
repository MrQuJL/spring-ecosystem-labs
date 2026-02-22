package com.spring.mongo.module.mongo.service;

import com.mongoplus.model.PageResult;
import com.mongoplus.service.IService;
import com.spring.mongo.module.mongo.dto.UserDTO;
import com.spring.mongo.module.mongo.dto.UserPageQuery;
import com.spring.mongo.module.mongo.dto.UserStatusReq;
import com.spring.mongo.module.mongo.entity.User;
import com.spring.mongo.module.mongo.vo.UserVO;

/**
 * 用户服务接口
 *
 * @author qujianlei
 * @since 1.0.0
 */
public interface IUserService extends IService<User> {
    
    /**
     * 新增用户
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    boolean addUser(UserDTO userDTO);

    /**
     * 更新用户信息
     *
     * @param userDTO 用户信息
     * @return 是否成功
     */
    boolean updateUser(UserDTO userDTO);

    /**
     * 更新用户状态
     *
     * @param req 状态更新请求
     * @return 是否成功
     */
    boolean updateStatus(UserStatusReq req);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    boolean deleteUser(String id);

    /**
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情(VO)
     */
    UserVO getUser(String id);

    /**
     * 分页查询用户
     *
     * @param query 查询条件
     * @return 分页结果(VO)
     */
    PageResult<UserVO> pageList(UserPageQuery query);
}