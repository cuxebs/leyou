package com.leyou.item.service;

import com.leyou.common.exception.LyException;
import com.leyou.common.exception.LyExceptionEnum;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenyilei
 * @date 2018/11/08-20:09
 * hello everyone
 */
@Service
@Slf4j
public class SpecificationService {
    @Autowired
    SpecGroupMapper specGroupMapper;

    @Autowired
    SpecParamMapper specParamMapper;

    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> select = specGroupMapper.select(specGroup);

        if(CollectionUtils.isEmpty(select)){
            throw new LyException(LyExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }

        return select;
    }

    public List<SpecParam> queryParamsByGid(Long gid) {
        SpecParam specParam =new SpecParam();
        specParam.setGroupId(gid);
        List<SpecParam> select = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(select)){
            throw new LyException("param 为空");
        }
        return select;
    }

    public List<SpecParam>  queryParamList(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam =new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);

        List<SpecParam> select = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(select)){
            throw new LyException("param 为空cid:"+cid+"--gid:"+gid);
        }
        return select;
    }

    public List<SpecGroup> queryGroupsExByCid(Long cid) {
        List<SpecGroup> specGroups = queryGroupsByCid(cid);
        List<SpecParam> specParams = queryParamList(null, cid, null, null);

        Map<Long,List<SpecParam>> map= new HashMap<>();
        specParams.forEach( param->{
            if(!map.containsKey(param.getGroupId())){
                map.put(param.getGroupId(),new ArrayList<>());
            }
            map.get(param.getGroupId()).add(param);
        } );

        specGroups.forEach(group->{
            group.setParams( map.get(group.getId()) );
        });
        specGroups.forEach(System.out::println);
        return specGroups;
//        Set<Long> groupIdList = specGroups.stream().map(x -> x.getId()).collect(Collectors.toSet());
//        for (Long groupId : groupIdList) {
//
//            List<SpecParam> specParams1 = specParams.stream().map(x -> {
//                if (x.getGroupId().equals(groupId)) {
//                    return x;
//                } else {
//                    return null;
//                }
//            }).collect(Collectors.toList());
//
//            specGroups.forEach(x->{
//                if(x.getId().equals(groupId)){
//                    x.setParams(specParams1);
//                }
//            });
//        }
//
    }
}
