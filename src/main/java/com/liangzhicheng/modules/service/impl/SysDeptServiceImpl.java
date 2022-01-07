package com.liangzhicheng.modules.service.impl;

import com.github.pagehelper.PageInfo;
import com.liangzhicheng.common.enums.StringEnum;
import com.liangzhicheng.common.response.WrapperHelper;
import com.liangzhicheng.common.utils.*;
import com.liangzhicheng.modules.entity.SysDeptEntity;
import com.liangzhicheng.modules.entity.dto.SysDeptDTO;
import com.liangzhicheng.modules.entity.query.SysDeptQueryCondition;
import com.liangzhicheng.modules.entity.vo.SysDeptDescVO;
import com.liangzhicheng.modules.entity.vo.SysDeptVO;
import com.liangzhicheng.modules.mapper.ISysDeptMapper;
import com.liangzhicheng.modules.service.ISysDeptService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门信息表 服务实现类
 * </p>
 *
 * @author liangzhicheng
 */
@Service
public class SysDeptServiceImpl extends BaseServiceImpl<ISysDeptMapper, SysDeptEntity> implements ISysDeptService {

    /**
     * 部门列表
     * @param deptDTO
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> listDept(SysDeptDTO deptDTO, Pageable pageable) {
        SysDeptQueryCondition deptQuery = new SysDeptQueryCondition(deptDTO);
        super.pageHandle(pageable, deptQuery.getPageNum(), deptQuery.getPageSize());
        List<SysDeptEntity> deptList = baseMapper.selectList(
                WrapperHelper.getInstance().buildCondition(SysDeptEntity.class, deptQuery));
        PageInfo pageInfo = new PageInfo<>();
        List<?> records = null;
        if(ListUtil.sizeGT(deptList)){
            pageInfo = new PageInfo<>(deptList);
            records = BeansUtil.copyList(pageInfo.getList(), SysDeptVO.class);
        }
        return super.pageResult(records, pageInfo);
    }

    /**
     * 获取部门
     * @param deptDTO
     * @return SysDeptVO
     */
    @Override
    public SysDeptDescVO getDept(SysDeptDTO deptDTO) {
        SysDeptEntity dept = baseMapper.selectById(deptDTO.getId());
        AssertUtil.isFalse(ToolUtil.isNull(dept), "部门不存在");
        return BeansUtil.copyEntity(dept, SysDeptDescVO.class);
    }

    /**
     * 保存部门
     * @param deptDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDept(SysDeptDTO deptDTO) {
        String id = deptDTO.getId();
        String name = deptDTO.getName();
        String description = deptDTO.getDescription();
        SysDeptEntity dept = baseMapper.selectById(id);
        if(ToolUtil.isNull(dept)){
            dept = new SysDeptEntity();
            dept.setId(SnowFlakeUtil.get().nextId() + "");
        }
        if(ToolUtil.isNotBlank(name)){
            AssertUtil.isFalse(name.length() > 30, "部门名称字数过长");
            dept.setName(name);
        }
        if(ToolUtil.isNotBlank(description)){
            AssertUtil.isFalse(description.length() > 200, "部门描述字数过长");
            dept.setDescription(description);
        }
        saveOrUpdate(dept);
    }

    /**
     * 删除部门
     * @param deptDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDept(SysDeptDTO deptDTO) {
        this.lambdaUpdate()
                .eq(SysDeptEntity::getId, deptDTO.getId())
                .set(SysDeptEntity::getDelFlag, StringEnum.ONE.getValue())
                .update();
    }

}
