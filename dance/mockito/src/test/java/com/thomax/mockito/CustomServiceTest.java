package com.thomax.mockito;

import com.thomax.mockito.domain.CustomObject;
import com.thomax.mockito.mapper.CustomMapper;
import com.thomax.mockito.service.CustomService;
import com.thomax.mockito.service.impl.CustomServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomServiceTest {

    /*
    * @Spy与@Mock的区别：
    *
    * 1.默认行为不同
    *   对于未指定mock的方法，spy默认会调用真实的方法，有返回值的返回真实的返回值，而mock默认不执行，有返回值的，默认返回null
    * 2.使用方式不同
    *   Spy中用when...thenReturn私有方法总是被执行，预期是私有方法不应该执行，因为很有可能私有方法就会依赖真实的环境。
    *   Spy中用doReturn..when才会不执行真实的方法。
    *   mock中用 when...thenReturn 私有方法不会执行。
    * 3.代码统计覆盖率不同
    *   @spy使用的真实的对象实例，调用的都是真实的方法，所以通过这种方式进行测试，在进行sonar覆盖率统计时统计出来是有覆盖率；
    *   @mock出来的对象可能已经发生了变化，调用的方法都不是真实的，在进行sonar覆盖率统计时统计出来的Calculator类覆盖率为0.00%
    */
    //@Spy
    @Mock
    private CustomMapper customMapper;

    @InjectMocks
    private CustomService customService = new CustomServiceImpl();

    @Test
    public void test01() {
        //参数类型String
        when(customMapper.test01("1")).thenReturn("one");
        when(customMapper.test01("2")).thenReturn("two");
        System.out.println("ture return 1 -> mock return " + customService.test01("1"));
        System.out.println("ture return 2 -> mock return " + customService.test01("2"));
        System.out.println("ture return 3 -> mock return " + customService.test01("3"));

        //参数类型Object，需要重写equals()和hashCode()，或者直接使用@Data
        CustomObject obj = new CustomObject(1, "1", new BigDecimal(1));
        when(customMapper.test02(obj)).thenReturn("one");
        when(customMapper.test02(new CustomObject(2, "2", new BigDecimal(2)))).thenReturn("two");
        System.out.println("ture return object-1 -> mock return " + customService.test02(obj));
        System.out.println("ture return object-2 -> mock return " + customService.test02(new CustomObject(2, "2", new BigDecimal(2))));
        System.out.println("ture return object-2 -> mock return " + customService.test02(new CustomObject(3, "3", new BigDecimal(3))));

        //test
        Assert.assertEquals(customService.test01("1"), "one");
    }

}
