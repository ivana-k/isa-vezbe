package rs.ac.uns.ftn.informatika.springapp.controller;

import java.util.List;

import rs.ac.uns.ftn.informatika.springapp.domain.Asset;
import rs.ac.uns.ftn.informatika.springapp.service.AssetService;

public class AssetControllerConstructorDI {
	
	private AssetService assetService;
	
	//constructor-based dependency injection
	public AssetControllerConstructorDI(AssetService assetService) {
		this.assetService = assetService;
	}
	
	public List<Asset> getAssets(){
		return assetService.listAssets();
	}

}
