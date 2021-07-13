package com.zto.testcase.service;

import com.zto.testcase.request.UimetadataRequest;
import java.util.List;
import java.util.Map;

public interface UimetadataService {
    long insert(UimetadataRequest uimetadataRequest);

    List select(UimetadataRequest uimetadataRequest);

    int deleteById(UimetadataRequest uimetadataRequest);

    int updateById(UimetadataRequest uimetadataRequest);

    Map tree(UimetadataRequest uimetadataRequest);
}
