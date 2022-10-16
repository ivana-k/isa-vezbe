package rs.ac.uns.ftn.informatika.springapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import rs.ac.uns.ftn.informatika.springapp.domain.Asset;
import rs.ac.uns.ftn.informatika.springapp.service.AssetService;

@Controller
public class AssetControllerSetterDI {

	private AssetService assetService;

	//setter-based dependency injection
	@Autowired
	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}
	
	public List<Asset> getAssets(){
		return assetService.listAssets();
	}

}
