package com.it.uaa.shiro;

import com.it.uaa.common.infrastructure.shiro.IAbstractRealm;
import com.it.uaa.domain.User;
import com.it.uaa.repository.UserRepository;
import org.apache.shiro.authc.*;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Auther: lxr
 * @Date: 2018/9/30 10:01
 * @Description:
 */
@Component("realm")
public class ImiShiroRealm  extends IAbstractRealm {

    private final UserRepository userRepository;

    public ImiShiroRealm(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AuthenticationInfo getUser(String username) {
        return  userRepository.findByUsername(username)
                .map(this::checkUser)
                 .orElseThrow(()->new UnknownAccountException("账号不存在"));
    }

    @Override
    public Set<String> getRoles(String username) {
        return null;
    }

    @Override
    public Set<String> getPermissions(String username) {
        return null;
    }

    public SimpleAuthenticationInfo checkUser(User user){
            if(user.isDelete()){
                throw new DisabledAccountException("账号已删除");
            }
            else if(!user.isStatus()){
                throw new LockedAccountException("账号已经锁定");
            }
            return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword().getBytes(),getName());
    }

}
