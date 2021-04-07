package singleTone.lazySingleTon;

/** 懒汉式单例模式
 * @author hyc
 * @date 2021/4/7
 */
public class LazySingleTon {

    private static LazySingleTon instance;

    //构造器私有化
    private LazySingleTon(){

    }
    //需要使用同步方法来保证线程安全
    private static synchronized LazySingleTon getInstance(){
        if(instance == null){
            instance = new LazySingleTon();
        }
        return instance;
    }

}
