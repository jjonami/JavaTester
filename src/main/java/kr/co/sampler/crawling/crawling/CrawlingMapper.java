package kr.co.sampler.crawling.crawling;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrawlingMapper {
    int insert(CrawlingData data);
}