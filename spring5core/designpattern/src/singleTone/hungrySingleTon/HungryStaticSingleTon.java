package singleTone.hungrySingleTon;

/** 饿汉式单例模式的另一种写法是使用静态代码块
 * @author hyc
 * @date 2021/4/6
 */
public class HungryStaticSingleTon {

    private static final HungryStaticSingleTon instance;
    //构造器私有化是单例模式的特征
    private HungryStaticSingleTon(){

    }
    //使用静态代码块来实例化
    static {
        instance = new HungryStaticSingleTon();
    }
    private static HungryStaticSingleTon getInstance(){
        return instance;
    }
}
