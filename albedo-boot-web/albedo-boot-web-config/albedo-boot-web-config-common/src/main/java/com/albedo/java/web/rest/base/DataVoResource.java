package com.albedo.java.web.rest.base;

import com.albedo.java.common.domain.base.DataEntity;
import com.albedo.java.common.service.DataVoService;
import com.albedo.java.util.PublicUtil;
import com.albedo.java.util.domain.Globals;
import com.albedo.java.vo.base.DataEntityVo;
import com.albedo.java.web.rest.ResultBuilder;
import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 基础控制器支持类 copyright 2014 albedo all right reserved author MrLi created on 2014年10月15日 下午4:04:00
 */
public class DataVoResource<Service extends DataVoService, V extends DataEntityVo>
        extends BaseResource {

    @Autowired
    protected Service service;

    @ModelAttribute
    public V getAttribute(@RequestParam(required = false) String id) throws Exception {
        String path = request.getRequestURI();
        if (path != null && !path.contains(Globals.URL_CHECKBY) && !path.contains(Globals.URL_FIND) &&
                PublicUtil.isNotEmpty(id)) {
            return (V) service.findOneVo(id);
        } else {
            return (V) service.getEntityVoClz().newInstance();
        }
    }

    /**
     * @param id
     * @return
     */
    @GetMapping("/{id:" + Globals.LOGIN_REGEX + "}")
    @Timed
    public ResponseEntity get(@PathVariable String id) {
        log.debug("REST request to get Entity : {}", id);
        return ResultBuilder.buildOk(service.findOneById(id)
            .map(item -> service.copyBeanToVo((DataEntity) item)));
    }

    @ResponseBody
    @GetMapping(value = "checkByProperty")
    public boolean checkByProperty(V entityForm) {
        return service.doCheckByProperty(entityForm);
    }

    @ResponseBody
    @GetMapping(value = "checkByPK")
    public boolean checkByPK(V entityForm) {
        return service.doCheckByPK(entityForm);
    }

//	@RequestMapping(value = "findJson")
//	public void findJson(ComboSearch combo, HttpServletResponse response) {
//
//		List<ComboData> comboDataList = jpaCustomeRepository.findJson(combo);
//		writeJsonHttpResponse(comboDataList, response);
//	}

}
