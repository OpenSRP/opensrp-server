package org.opensrp.repository.postgres.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.opensrp.domain.postgres.AlertMetadata;
import org.opensrp.domain.postgres.AlertMetadataExample;

public interface AlertMetadataMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core.alert_metadata
     *
     * @mbg.generated Fri Mar 09 15:24:01 EAT 2018
     */
    long countByExample(AlertMetadataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core.alert_metadata
     *
     * @mbg.generated Fri Mar 09 15:24:01 EAT 2018
     */
    int deleteByExample(AlertMetadataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core.alert_metadata
     *
     * @mbg.generated Fri Mar 09 15:24:01 EAT 2018
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core.alert_metadata
     *
     * @mbg.generated Fri Mar 09 15:24:01 EAT 2018
     */
    int insert(AlertMetadata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core.alert_metadata
     *
     * @mbg.generated Fri Mar 09 15:24:01 EAT 2018
     */
    int insertSelective(AlertMetadata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core.alert_metadata
     *
     * @mbg.generated Fri Mar 09 15:24:01 EAT 2018
     */
    List<AlertMetadata> selectByExample(AlertMetadataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core.alert_metadata
     *
     * @mbg.generated Fri Mar 09 15:24:01 EAT 2018
     */
    AlertMetadata selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core.alert_metadata
     *
     * @mbg.generated Fri Mar 09 15:24:01 EAT 2018
     */
    int updateByExampleSelective(@Param("record") AlertMetadata record, @Param("example") AlertMetadataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core.alert_metadata
     *
     * @mbg.generated Fri Mar 09 15:24:01 EAT 2018
     */
    int updateByExample(@Param("record") AlertMetadata record, @Param("example") AlertMetadataExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core.alert_metadata
     *
     * @mbg.generated Fri Mar 09 15:24:01 EAT 2018
     */
    int updateByPrimaryKeySelective(AlertMetadata record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core.alert_metadata
     *
     * @mbg.generated Fri Mar 09 15:24:01 EAT 2018
     */
    int updateByPrimaryKey(AlertMetadata record);
}