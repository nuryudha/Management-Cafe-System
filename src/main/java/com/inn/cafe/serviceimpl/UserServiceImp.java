package com.inn.cafe.serviceimpl;

import com.inn.cafe.constents.CafeConstant;
import com.inn.cafe.dao.UserDao;
import com.inn.cafe.pojo.User;
import com.inn.cafe.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.service.UserService;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}",requestMap);
        if(validateSignUpMap(requestMap)) {
            User user = userDao.findByEmailId(requestMap.get("email"));
//            TODO : Melanjutkan BRO
            if(Objects.isNull(user)){
//                userDao.save()
            }else{
                return CafeUtils.getResponseEntity("Email already exist",HttpStatus.BAD_REQUEST);
            }
        }else{
            return CafeUtils.getResponseEntity(CafeConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
        return  null;
    }

    private boolean validateSignUpMap(Map<String,String> requestMap){
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                &&requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }
        return false;
    }
}
