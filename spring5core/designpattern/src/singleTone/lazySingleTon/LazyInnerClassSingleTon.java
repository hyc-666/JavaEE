package singleTone.lazySingleTon;

/** 内部类实现单例模式,既不会提前加载,也没有加锁的操作
 * 如果当前类没有使用,那么其内部类也不会先加载
 * 如果使用到这个类,则会加载内部类
 * @author hyc
 * @date 2021/4/7
 */
public class LazyInnerClassSingleTon {

    private LazyInnerClassSingleTon(){

    }

    private static class LazyHolder{
        private static final LazyInnerClassSingleTon INSTANCE = new LazyInnerClassSingleTon();
    }

    public static final LazyInnerClassSingleTon getInstance(){
        return LazyHolder.INSTANCE;
    }
}
