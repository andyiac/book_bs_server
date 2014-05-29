package com.androidbook.test;

import com.androidbook.ResponseParam.BulidResponseParam;
import com.androidbook.requesthandle.HandleRequest;
import com.androidbook.requesthandle.HandleRequestFactory;
import com.androidbook.requesthandle.Login;
import com.androidbook.requesthandle.Signin;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by summer on 5/20/14.
 *
 * begin use TDD
 *
 * enjoy it !!
 */
public class DBTest {


    private int uid = 123;
    private String password = "123";
    private String[] params= null;

    public void mTestSignin(){

        String[] params ={"124","123","123","123","123","123","123"};

        Signin signin = new Signin();

        int a = signin.handleRequest(123,"123",params);

        System.out.println("========mTestSigin======"+a);
        Assert.assertEquals(0,a);
    }

    /**
     * 只测试数据库业务逻辑
     */
    @Test
    public void mTestLogin(){

        String [] params = {"张宁","123"};

        Login login = new Login();
        int i = login.handleRequest(uid,password,null);
        Assert.assertEquals(0, i);

    }

    /**
     *测试从请求到返回数据的业务逻辑
     */
    @Test
    public void mTestLogin2(){

        String requestType = "Login";
        HandleRequest handle = HandleRequestFactory
                .getHandleRequestInstance(requestType);
        int result = handle.handleRequest(
                uid,password,null);
        String param = handle.getResponseParam();
        String responseParam = BulidResponseParam.getInstance()
                .bulidParam(result, requestType, param);
        System.out.println("===========responseParam====>>>"+responseParam);
        Assert.assertEquals(0,result);
    }

    @Test
    public void mCracklePop(){
        for(int i=1 ;i<=100 ;i++ ){
           String m = "";
           if( i%3==0 && i%5 !=0){
               m = "crackle";
           }else if(i%5==0 && i%3 !=0){
               m ="pop";
            }else if(i%5==0 && i%3 ==0){
               m = "CracklePop";
           } else {
               m = String.valueOf(i);
            }
            System.out.println("====>>"+m);
        }
    }


}
