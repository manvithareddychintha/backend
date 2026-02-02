package com.recursion.portfolioManager.models;

import lombok.Data;

@Data
public class ApiResult {
    private boolean success;
    private Object object;

    public ApiResult(boolean success, Object object)
    {
        this.success=success;
        this.object=object;
    }

}
