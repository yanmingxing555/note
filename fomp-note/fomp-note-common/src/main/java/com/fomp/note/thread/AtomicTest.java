package com.fomp.note.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 解决并发的线程安全问题有两种方式：
 * 1、等待唤醒机制
 * 如果抢不到锁，就将线程挂起，当锁释放的时候，然后将其唤醒重新抢锁。
 * 2、自旋CAS
 * 自旋就是设置循环CAS抢锁的意思，当CAS成功的时候才会退出循环
 *
 *      名称	                适用场景
 * 等待唤醒机制	   当长时间都无法抢到锁的时候，还是将线程挂起，然后等待唤醒的好。因为等待和唤醒牵扯到线程挂起和切换，
 *                  会导致从用户态到内核态的切换，并且线程切换会导致上下文的切换，现场保存什么的，会比较浪费资源
 * 自旋CAS	       当短时间内就可以获取到锁的时候，自旋CAS比较合适，短时间的自旋CAS肯定会比线程切换消耗的资源要少，
 *                  如果要是时间长的话，就不太划算了，因为自旋CAS会一直占用CPU
 */
/**
 * 原子类：Atomic原子类就是利用自旋CAS来保证线程安全。
 * 实现：
 *      JVM中的CAS操作正是利用了处理器提供的CMPXCHG指令实现的。
 *      自旋CAS实现的基本思路就是循环进行CAS操作直到成功为止；
 *      Atomic包里的类基本都是使用Unsafe实现的包装类
 * 四种更新方式：
 *      1.原子更新基本类型：AtomicInteger、AtomicLong、AtomicBoolean
 *      2.原子更新数组：AtomicIntegerArray、AtomicLongArray、AtomicReferenceArray
 *      3.原子更新引用：AtomicReference、AtomicReference的ABA实例、AtomicStampedRerence、AtomicMarkableReference；
 *      4.原子更新字段：AtomicIntegerFieldUpdater、AtomicLongFieldUpdater、AtomicReferenceFieldUpdater
 */
public class AtomicTest {
    /********************
     * 1、基本类型：
     *  AtomicInteger、AtomicBoolean、AtomicLong
     * 方法：
     * getAndIncrement() // 原子化 i++
     * getAndDecrement() // 原子化的 i--
     * incrementAndGet() // 原子化的 ++i
     * decrementAndGet() // 原子化的 --i
     * getAndAdd(delta) // 当前值 +=delta，返回 += 前的值
     * addAndGet(delta) // 当前值 +=delta，返回 += 后的值
     * compareAndSet(expect, update) //CAS 操作，返回是否成功
     * // 以下四个方法
     * // 新值可以通过传入 func 函数来计算
     * getAndUpdate(func)
     * updateAndGet(func)
     * getAndAccumulate(x,func)
     * accumulateAndGet(x,func)
     * ***********************/
    int age = 0;
    Lock lock = new ReentrantLock();
    AtomicInteger ageAtomic = new AtomicInteger(0);
    //加锁方式
    public void setAgeByLock(){
        lock.lock();
        age++;
        lock.unlock();
    }
    //原子类方式
    public void setAgeByAtomic(){
        ageAtomic.getAndIncrement();
    }
    /********
     * 2.原子更新数组类型
     *
     * 通过原子的方式更新数组里的某个元素，Atomic包提供了以下3个类。
     *     AtomicIntegerArray : 原子更新整型数组里的元素
     *     AtomicLongArray : 原子更新长整型数组里的元素
     *     AtomicReferenceArray : 原子更新引用类型数组里的元素
     * 常用方法：
     * ① int addAndGet（int i，int delta）：以原子方式将输入值与数组中索引i的元素相加
     * ② boolean compareAndSet（int i，int expect，int update）：如果当前值等于预期值，
     *      则以原子方式将数组位置i的元素设置成update值
     ********/
    private static int[] array = new int[]{1, 2, 3};
    private static AtomicIntegerArray integerArray = new AtomicIntegerArray(array);
    public void testArray(){
        //对数组中索引为1的位置的元素加10
        int result = integerArray.getAndAdd(1, 10);
        System.out.println(result);
        System.out.println(integerArray.get(1));
    }

    /********
     * 3.对象引用类型：
     *      AtomicReference利用CAS来更新引用，旧值为原来的引用对象，
     *      新值为新的引用对象。 是更新引用，而不是更新对象。
     *  AtomicReference：原子更新引用类型
     *  AtomicReferenceFieldUpdater：原子更新引用类型里的字段
     *  AtomicMarkableReference：原子更新带有标记位的引用类型。可以原子更新一个布尔类型的标记位和引用类型
     ********/
    private static AtomicReference<User> reference = new AtomicReference<>();
    public void testAtomicReference(){
        User user1 = new User("hello", 20);
        reference.set(user1);
        User user2 = new User("world",30);
        User user = reference.getAndSet(user2);
        System.out.println(user);
        System.out.println(reference.get());
    }

    /*********
     * 4.原子更新字段类型
     * 常用方法
     * 如果需原子地更新某个类里的某个字段时，就需要使用原子更新字段类。
     * Atomic包提供了以下3个类进行原子字段更新
     *     AtomicIntegerFieldUpdater：原子更新整型的字段的更新器。
     *     AtomicLongFieldUpdater：原子更新长整型字段的更新器。
     *     AtomicStampedReference：原子更新带有版本号的引用类型。该类将整数值与引用关联起来，
     *          可用于原子的更新数据和数据的版本号，可以解决使用CAS进行原子更新时可能出现的ABA问题。
     * 使用注意事项：
     *     第一：因为原子更新字段类都是抽象类，每次使用的时候必须使用静态方法newUpdater()创建一个更新器，
     *              并且需要设置想要更新的类和属性
     *     第二：更新类的字段（属性）必须使用public volatile修饰符
     *********/
    private static AtomicIntegerFieldUpdater updater =
            AtomicIntegerFieldUpdater.newUpdater(User.class,"age");
    public void testField(){
        User user = new User("a", 10);
        int oldValue = updater.getAndAdd(user, 5);
        System.out.println(oldValue);
        System.out.println(updater.get(user));
    }

    public static void main(String[] args) {
        AtomicTest test = new AtomicTest();
        test.testField();
    }


}
class User{
    private String name;
    public volatile int age;
    public User() {
    }
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
