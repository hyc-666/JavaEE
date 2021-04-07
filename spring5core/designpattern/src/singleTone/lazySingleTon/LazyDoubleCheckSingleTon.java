package singleTone.lazySingleTon;

/** 双重检查锁的单例模式
 * @author hyc
 * @date 2021/4/7
 */
public class LazyDoubleCheckSingleTon {
    //双重检查锁的单例效率比同步方法高
    private static volatile LazyDoubleCheckSingleTon instance;

    private LazyDoubleCheckSingleTon(){

    }

    public static LazyDoubleCheckSingleTon getInstance(){
        if (instance == null){
            synchronized (LazyDoubleCheckSingleTon.class){
                if (instance == null){
                    instance = new LazyDoubleCheckSingleTon();
                }
            }
        }
        return instance;
    }
}
