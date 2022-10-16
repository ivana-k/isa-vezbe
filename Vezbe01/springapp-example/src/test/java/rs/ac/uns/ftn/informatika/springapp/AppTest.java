package rs.ac.uns.ftn.informatika.springapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import rs.ac.uns.ftn.informatika.springapp.config.AppConfiguration;
import rs.ac.uns.ftn.informatika.springapp.controller.AssetController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class})
public class AppTest {
	
	@Autowired
	AssetController assetController;

	@Test
	public void testGetAssets() throws Exception {

		assertEquals(2, assetController.getAssets().size());
	}

}
