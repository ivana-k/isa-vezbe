package ftn.isa.controller;


import ftn.isa.model.Asset;
import ftn.isa.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AssetController {

    // koja tacno implementacija ce biti injektovana?
    private AssetService assetService;

    /**
     * kada postoji samo jedan konstruktor sa parametrima, spring je dovoljno pametan da odradi autowiring
     * nije potrebno dodavati @Autowired anotaciju
     * @Qualifier - specificira naziv bean-a koji je potrebno injektovati
     * @param assetService
     */
    public AssetController(@Qualifier("firstService") AssetService assetService) {
        this.assetService = assetService;
    }

    /*
    alternativno, setter injection
    @Autowired
    public void setAssetService(AssetService assetService) {
        this.assetService = assetService;
    }*/

    public List<Asset> getAssets() {
        return assetService.listAssets();
    }
}
