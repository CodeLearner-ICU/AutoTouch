package java.util;

/**
 * This List is extends from ArrayList
 * 继承自ArrayList
 * <p>
 * When list size() > 0 and The User get(index) the object out of index for List from list,Don't throw IndexOutOfBoundsException
 * And restart get Value List from index = 0
 * 当数组长度大于0
 * 且用户取的索引出现索引越界时，不会抛出异常，会从重新索引0开始继续取
 * <p>
 * Avoid index transgressions（Avoid IndexOutOfBoundsException）
 * 避免索引越界
 * <p>
 * Pay attention to get index value
 * 注意取值逻辑
 *
 *
 * @author Damon Lee CN
 * @see ArrayList
 * @since 1.2
 */
public class LoopArrayList<E> extends ArrayList<E> {
    @Override
    public E get(int index) {
        if (size() != 0 && index >= size()) {
            // KEY POINT
            return super.get(index % size());
        } else {
            return super.get(index);
        }
    }
}