package ftn.isa.service.impl;

import ftn.isa.model.Asset;
import ftn.isa.service.AssetService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("secondService")
public class AssetServiceImpl2 implements AssetService {

    @Override
    public List<Asset> listAssets() {
        return new ArrayList<>();
    }
}
