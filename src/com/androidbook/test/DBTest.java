package com.androidbook.test;

import com.androidbook.requesthandle.Signin;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by summer on 5/20/14.
 */
public class DBTest extends TestCase {
    @Test
    private void mTestSignin(){
        String[] params ={"123","123","123"};
        Signin signin = new Signin();
        signin.handleRequest(123,"123",params);
    }
}
