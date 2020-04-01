package com.github.guang19.knife.sensitivefilter.sensitivewordfilter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangguang
 * @date 2020/3/30
 *
 * <p>
 *  敏感词库
 * </p>
 */
public class DefaultSensitiveWordRepository implements SensitiveWordRepository
{

    //存放敏感词树的桶
    private final Map<Character,Object> sensitiveWordMap;

    //敏感词数量
    private int size;

    //敏感词树的某个敏感词的结尾标志
    private static final String END_FLAG = "end";

    /**
     * 构造空敏感词库
     */
    public DefaultSensitiveWordRepository()
    {
        this.sensitiveWordMap = new HashMap<>(64);
    }

    /**
     * 构造指定初始化容量的空敏感词库
     * @param initCapacity  指定的初始化容量
     */
    public DefaultSensitiveWordRepository(int initCapacity)
    {
        this.sensitiveWordMap = new HashMap<>(initCapacity);
    }

    /**
     * 向敏感词库里添加敏感词
     * @param sensitiveWord 敏感词
     */
    @Override
    public void addSensitiveWord(String sensitiveWord)
    {
        buildSensitiveWordMapTree(sensitiveWord,true);
    }

    /**
     * 从敏感词库中删除敏感词
     * @param sensitiveWord 要删除的敏感词
     */
    @Override
    public void removeSensitiveWord(String sensitiveWord)
    {
        buildSensitiveWordMapTree(sensitiveWord,false);
    }


    //更新敏感词树
    private void buildSensitiveWordMapTree(String sensitiveWord,boolean addOrDelete)
    {
        //敏感词不能为空
        if(sensitiveWord == null || sensitiveWord.isEmpty() || (sensitiveWord = sensitiveWord.trim()).isEmpty())
        {
            return;
        }
        char[] sensitiveWordChars = sensitiveWord.toCharArray();
        if(addOrDelete)
        {
            addSensitiveWordToMapTree(sensitiveWordMap,sensitiveWordChars,sensitiveWordChars.length);
        }
        else
        {
            removeSensitiveWordFromMapTree(sensitiveWordMap,sensitiveWordChars,sensitiveWordChars.length);
        }
    }


    //向敏感词树添加敏感词
    @SuppressWarnings({"rawtypes","unchecked"})
    private void addSensitiveWordToMapTree(Map searchMap , char[] sensitiveWordCharArr,int length)
    {
        char keyChar;
        Object wordMapTree = null;
        Map wordMapTreeNode = null;

        //遍历当前敏感词的每个字符，构造敏感词树
        for (int i = 0 ; i < length; ++i)
        {
            keyChar = sensitiveWordCharArr[i];

            //根据当前字符获取敏感词所在的敏感词树 在敏感词树桶中的位置
            wordMapTree = searchMap.get(keyChar);

            //如果当前字符在某个敏感词树的节点中，就证明当前敏感词可能在这颗敏感词树中，
            //就继续向下搜索
            if(wordMapTree != null)
            {
                searchMap = ((Map)wordMapTree);
            }
            else
            {
                //如果当前敏感词不在敏感词树中,就以当前字符为节点添加到敏感词树中去
                wordMapTreeNode = new HashMap<>();
                wordMapTreeNode.put(END_FLAG,false);
                searchMap.put(keyChar,wordMapTreeNode);
                searchMap = wordMapTreeNode;
            }
            //如果当前字符是敏感词的最后一个字符
            if (i == length - 1)
            {
                //如果当前敏感词是没有添加过的,就更新敏感词数量并设置END标志为true
                if(!(boolean)searchMap.get(END_FLAG))
                {
                    ++size;
                    searchMap.put(END_FLAG,true);
                }
            }
        }
    }


    //从敏感词树中移除敏感词
    @SuppressWarnings({"rawtypes","unchecked"})
    private void removeSensitiveWordFromMapTree(Map searchMap , char[] sensitiveWordCharArr,int length)
    {
        char keyChar = 0;

        //当前节点的上一层节点
        Map prev , temp;
        prev = temp = searchMap;

        //判断同脉敏感词的位置
        int isEndFlag = 0;

        //遍历当前敏感词的每个字符，直到最后一个字符
        for (int i = 0 ; i < length; ++i)
        {
            keyChar = sensitiveWordCharArr[i];
            prev = searchMap;
            //如果当前字符不在敏感词树中就直接返回
            if((searchMap = (Map) searchMap.get(keyChar)) == null)
            {
                //gc
                temp = prev = null;
                return;
            }
            //防止到达末尾还判断
            if (i < length - 1 && (boolean)searchMap.get(END_FLAG))
            {
                isEndFlag = i + 1;
            }
        }

        //遍历到达末尾了
        if((boolean)searchMap.get(END_FLAG))
        {
              //即:
              //   你(end=false)
              //   真(end=false)
              //   是(end=false)
              //   个 (end=false)
              //   大(end=false)
             //   傻(end=false)
            //    子(end=true)   要删除的敏感词为: 你真是个大傻子
            //    呀(end=true)
            if(searchMap.size() > 1)
            {
                searchMap.put(END_FLAG,false);
            }
            else
            {

                //即:
                //   你(end=false)
                //   真(end=false)
                //   是(end=false)
                //   个 (end=false)
                //   大(end=false)
                //   傻(end=false)
                //   子(end=true)   要删除的敏感词为: 你真是个大傻子呀
                //   呀(end=true)    //直接删除: '呀' 就行了
                if(prev.get(END_FLAG) == null || (boolean)prev.get(END_FLAG))
                {
                    prev.remove(keyChar);
                }

                //如果prev的end为false就说明:
                //你真是个大傻子(end=false)呀(end=true)
                //那我们就需要知道 : 你真是个大傻子呀 的哪个字符的end是true,需要重新遍历
                else
                {
                    prev = temp;

                    //如果isEndFlag == 0
                    //即:
                    //         你(end=false)
                    //       真(end=false)       ...
                    //      是(end=false)         ...
                    //    个 (end=false)
                    //   大(end=false)
                    //  傻(end=false)
                    // 子(end=false)
                    //呀(end=true)
                    //要删除的敏感词为: 你真是个傻子呀
                    //isEndFlag == 0 代表没有end=true的节点
                    if(isEndFlag == 0)
                    {
                        prev = (Map)prev.get(sensitiveWordCharArr[isEndFlag]);
                        //即:
                        //              你(end=false)
                        //       真(end=false)       傻(end=false)
                        //      是(end=false)            ...
                        //    个 (end=false)
                        //   大(end=false)
                        //  傻(end=false)
                        // 子(end=false)
                        //呀(end=true)
                        if(prev.size() > 2)
                        {
                            //删除: '真' 就行了
                            prev.remove(sensitiveWordCharArr[isEndFlag+1]);
                        }
                        //即:
                        //   你(end=false)
                        //   真(end=false)
                        //   是(end=false)
                        //   个 (end=false)
                        //   大(end=false)
                        //   傻(end=false)
                        //   子(end=false)
                        //   呀(end=true)
                        else
                        {
                            //直接删除: '你'
                            temp.remove(sensitiveWordCharArr[isEndFlag]);
                        }
                    }
                    //
                    //即:
                    //   你(end=false)
                    //   真(end=false)
                    //   是(end=false)
                    //   个(end=true)    //从此处删除就行了
                    //   大(end=false)
                    //   傻(end=false)
                    //   子(end=false)
                    //   呀(end=true)
                    //要删除的敏感词为: 你真是个大傻子呀
                    else
                    {
                        for (int i = 0 ; i < isEndFlag; ++i)
                        {
                            prev = (Map) prev.get(keyChar = sensitiveWordCharArr[i]);
                        }
                        prev.remove(sensitiveWordCharArr[isEndFlag]);
                    }
                }
            }
        }
        --size;
        //gc
        temp = prev = searchMap = null;
    }



    /**
     * 获取敏感词库
     *
     * @return 敏感词库
     */
    @Override
    public Map<Character, Object> getSensitiveWordRepository()
    {
        return sensitiveWordMap;
    }

    /**
     * 获取敏感词树结尾标志
     *
     * @return 敏感词树结尾标志
     */
    @Override
    public String getEndFlag()
    {
        return END_FLAG;
    }

    /**
     * 返回敏感词库中敏感词的数量
     * @return  敏感词数量
     */
    @Override
    public int sensitiveWordRepositorySize()
    {
        return size;
    }
}
