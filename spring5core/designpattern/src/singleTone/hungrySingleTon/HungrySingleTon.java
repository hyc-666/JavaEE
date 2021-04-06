package singleTone.hungrySingleTon;

/** 单例模式
 * 饿汉式单例模式有两种写法
 * 优点:饿汉式单例模式在类加载的时候就创建好了,本身创建要早于线程出现
 *    因此本身是线程安全的,也没有加锁等操作,执行效率较高
 * 缺点:有可能用不到,因此会浪费一定的资源
 * @author hyc
 * @date 2021/4/6
 */
public class HungrySingleTon {
    //一种是使用静态属性+静态方法
    //实例化的时候使用就地实例化的方法
    private static final HungrySingleTon instance = new HungrySingleTon();

    private HungrySingleTon(){

    }

    public static HungrySingleTon getInstance(){
        return instance;
    }
}
