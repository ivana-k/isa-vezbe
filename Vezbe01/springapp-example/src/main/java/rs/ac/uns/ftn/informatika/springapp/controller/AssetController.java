package rs.ac.uns.ftn.informatika.springapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import rs.ac.uns.ftn.informatika.springapp.domain.Asset;
import rs.ac.uns.ftn.informatika.springapp.service.AssetService;

/*
 * Klasa je anotirana sa @Controller sto treba da naznaci Springu da je klasa
 * Spring Bean i da treba da bude u nadleznosti Spring kontejnera.
 * Isti efekat bi se dobio koriscenjem anotacije @Component koja je
 * nadanotacija za @Controller.
 */
@Controller
public class AssetController {

	//field-based dependency injection
	@Autowired
	private AssetService assetService;

	public List<Asset> getAssets(){
		return assetService.listAssets();
	}
}
