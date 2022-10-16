package ftn.isa.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ftn.isa.model.Asset;
import ftn.isa.service.AssetService;

@Service
// @Scope("prototype")
@Qualifier("secondService")
public class AssetServiceImpl2 implements AssetService {

    @Override
    public List<Asset> listAssets() {
        return new ArrayList<>();
    }
}
