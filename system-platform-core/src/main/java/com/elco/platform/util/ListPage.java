package com.elco.platform.util;

import java.util.List;

/**
 * @author kay
 * @date 2021/8/18
 */
public class ListPage {
    /**
     * list 要分割的list
     * num 第几页
     * every 每页展示的个数
     * */
    public List listPage(List list,int num,int every){
        if(every*num<=list.size()){
            list=list.subList((num-1)*every,every*num);
        }else {
            list=list.subList((num-1)*every,list.size());
        }
        return list;
    }
}
