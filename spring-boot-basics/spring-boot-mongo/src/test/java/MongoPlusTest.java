import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mongoplus.model.PageParam;
import com.mongoplus.model.PageResult;
import com.spring.mongo.MongoApplication;
import com.spring.mongo.module.mongo.entity.User;
import com.spring.mongo.module.mongo.service.IUserService;

@SpringBootTest(classes = MongoApplication.class)
public class MongoPlusTest {
    
    @Autowired
    private IUserService userService;

    @Test
    public void testSelect() {
        System.out.println("----- selectAll method test ------");
        List<User> userList = userService.list();
        userList.forEach(System.out::println);
    }

    @Test
    public void testInsert() {
        System.out.println("----- insert method test ------");
        User user = new User();
        user.setName("小明");
        user.setAge(25L);
        user.setEmail("xiaoming@example.com");
        user.setBalance(new BigDecimal("1000.50"));
        user.setStatus(1);
        
        boolean result = userService.save(user);
        System.out.println("插入结果: " + result);
        System.out.println("插入的用户: " + user);
    }

    @Test
    public void testUpdate() {
        System.out.println("----- update method test ------");
        List<User> userList = userService.list();
        if (userList.isEmpty()) {
            System.out.println("没有用户数据，先插入一个");
            testInsert();
            userList = userService.list();
        }
        
        User user = userList.get(0);
        System.out.println("更新前的用户: " + user);
        
        user.setName("李四222");
        user.setAge(33L);
        user.setEmail("lisi@example.com");
        user.setBalance(new BigDecimal("2010.75"));
        
        boolean result = userService.updateById(user);
        System.out.println("更新结果: " + result);
        
        User updatedUser = userService.getById(user.getId());
        System.out.println("更新后的用户: " + updatedUser);
    }

    @Test
    public void testDelete() {
        System.out.println("----- delete method test ------");
        
        User user = new User();
        user.setName("测试用户");
        user.setAge(30L);
        user.setEmail("test@example.com");
        user.setBalance(new BigDecimal("500.00"));
        user.setStatus(1);
        
        boolean insertResult = userService.save(user);
        System.out.println("插入结果: " + insertResult);
        System.out.println("插入的用户: " + user);
        
        List<User> userList = userService.list();
        System.out.println("删除前用户数量: " + userList.size());
        
        boolean result = userService.removeById(user.getId());
        System.out.println("删除结果: " + result);
        
        List<User> newUserList = userService.list();
        System.out.println("删除后用户数量（逻辑删除后应该不变或变化较小）: " + newUserList.size());
        
        User deletedUser = userService.getById(user.getId());
        System.out.println("验证删除结果: " + (deletedUser == null ? "用户已成功删除（物理删除）" : "用户仍然存在（可能是逻辑删除）"));
    }
    
    @Test
    public void testPage() {
        System.out.println("----- page method test ------");
        
        List<User> existingUsers = userService.list();
        System.out.println("当前数据库用户数量: " + existingUsers.size());
        
        if (existingUsers.size() < 10) {
            System.out.println("数据不足，先批量插入10个测试用户");
            List<User> userList = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                User user = new User();
                user.setName("测试用户" + i);
                user.setAge(20L + i);
                user.setEmail("test" + i + "@example.com");
                user.setBalance(new BigDecimal(1000 + i * 100));
                user.setStatus(1);
                userList.add(user);
            }
            userService.saveBatch(userList);
            System.out.println("批量插入完成");
        }
        
        PageParam pageParam = new PageParam(1, 5);
        PageResult<User> pageResult = userService.page(pageParam);
        
        System.out.println("分页查询结果:");
        System.out.println("当前页: " + pageResult.getPageNum());
        System.out.println("每页条数: " + pageResult.getPageSize());
        System.out.println("总条数: " + pageResult.getTotalSize());
        System.out.println("总页数: " + pageResult.getTotalPages());
        System.out.println("是否第一页: " + pageResult.isFirstPage());
        System.out.println("是否最后一页: " + pageResult.isLastPage());
        System.out.println("是否有上一页: " + pageResult.isHasPreviousPage());
        System.out.println("是否有下一页: " + pageResult.isHasNextPage());
        System.out.println("当前页数据:");
        pageResult.getContentData().forEach(System.out::println);
    }
    
    @Test
    public void testPageWithPageNumAndSize() {
        System.out.println("----- page with pageNum and pageSize test ------");
        
        PageResult<User> pageResult = userService.page(2, 3);
        
        System.out.println("分页查询结果（第2页，每页3条）:");
        System.out.println("当前页: " + pageResult.getPageNum());
        System.out.println("每页条数: " + pageResult.getPageSize());
        System.out.println("总条数: " + pageResult.getTotalSize());
        System.out.println("总页数: " + pageResult.getTotalPages());
        System.out.println("当前页数据:");
        pageResult.getContentData().forEach(System.out::println);
    }
    
    @Test
    public void testPageList() {
        System.out.println("----- pageList method test ------");
        
        List<User> userList = userService.pageList(1, 5);
        
        System.out.println("pageList查询结果（不查询总数，速度更快）:");
        System.out.println("数据条数: " + userList.size());
        userList.forEach(System.out::println);
    }
}
