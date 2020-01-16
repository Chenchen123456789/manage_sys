package com.eim.project.system.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author chenchen
 */
@Data
@Accessors(chain = true)
public class SysHomeSetting {
    private Integer id;
    private String menuName;
    private String router;
    private String iconClass;
    private String frameSrc;
    private Integer isFrame;
    private String component;
}
