package rs.ac.uns.ftn.informatika.springapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.informatika.springapp.domain.Asset;

/*
 * Klasa je anotirana sa @Service sto treba da naznaci Springu da je klasa
 * Spring Bean i da treba da bude u nadleznosti Spring kontejnera.
 * Isti efekat bi se dobio koriscenjem anotacije @Component koja je
 * nadanotacija za @Service.
 */
@Service
public class AssetServiceImpl implements AssetService {

	
    public List<Asset> listAssets() {
        ArrayList<Asset> assets = new ArrayList<Asset>();
        assets.add(new Asset("Asset1", "Asset1 description"));
        assets.add(new Asset("Asset2", "Asset2 description"));
        return assets;
    }
}
