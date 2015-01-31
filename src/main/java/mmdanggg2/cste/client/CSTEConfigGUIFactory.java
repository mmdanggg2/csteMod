package mmdanggg2.cste.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTEInfo;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class CSTEConfigGUIFactory implements IModGuiFactory {
	
	public static class CSTEConfigGUI extends GuiConfig {
	    public CSTEConfigGUI(GuiScreen parent) {
	        super(parent, getConfigElements(), CSTEInfo.ID, false, false, GuiConfig.getAbridgedConfigPath(CSTE.config.toString()));
	    }
	    
	    private static List<IConfigElement> getConfigElements()
        {
            List<IConfigElement> list = new ArrayList<IConfigElement>();
            
            Iterator<String> catNameIterator = CSTE.config.getCategoryNames().iterator();
            
            while (catNameIterator.hasNext()){
            	String catStr = catNameIterator.next();
            	CSTELogger.logDebug("Adding catagory " + catStr + " to config GUI.");
            	
            	List<IConfigElement> confEntry = new ArrayList<IConfigElement>();
            	
            	Iterator<Property> catChildIterator = CSTE.config.getCategory(catStr).getOrderedValues().listIterator();
            	while (catChildIterator.hasNext()) {
            		Property childCatConf = catChildIterator.next();
            		CSTELogger.logDebug("Property in " + catStr + ": " + childCatConf.getName());
            		confEntry.add(new ConfigElement(childCatConf));
            	}
            	
            	list.add(new DummyCategoryElement(catStr, "cste.config." + catStr, confEntry));
            }
            
            return list;
        }
	}

	@Override
	public void initialize(Minecraft minecraftInstance) {}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return CSTEConfigGUI.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(
			RuntimeOptionCategoryElement element) {
		return null;
	}

}
