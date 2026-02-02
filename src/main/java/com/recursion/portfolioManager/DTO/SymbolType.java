package com.recursion.portfolioManager.DTO;


import com.recursion.portfolioManager.models.other.AssetType;
import lombok.Data;

@Data
public class SymbolType {
    private String symbol;
    private AssetType assetType;

    public SymbolType(String symbol,AssetType assetType)
    {
        this.symbol=symbol;
        this.assetType=assetType;
    }

    public SymbolType() { }
}
