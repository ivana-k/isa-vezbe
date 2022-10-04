package ftn.isa.service;

import ftn.isa.model.Asset;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AssetService {
    List<Asset> listAssets();
}
