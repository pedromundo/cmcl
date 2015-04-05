package soundrenderer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class SoundRenderer {

	public SoundRenderer() {
	}

	public void Render(String templateName, Map<String, Map> data)
			throws IOException, TemplateException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_20);
		cfg.setDirectoryForTemplateLoading(new File("./templates"));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		Template temp = cfg.getTemplate(templateName + ".ftl");

		long filename = Calendar.getInstance().getTime().getTime();

		FileWriter fr = new FileWriter("./" + filename + ".ly", false);

		temp.process(data, fr);
		Runtime.getRuntime().exec("lilypond " + filename + ".ly");

	}

	public void Render(String templateName, Map<String, Map> data,
			String fileName) throws IOException, TemplateException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_20);
		cfg.setDirectoryForTemplateLoading(new File("./templates"));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		Template temp = cfg.getTemplate(templateName + ".ftl");

		FileWriter fr = new FileWriter("./" + fileName + ".ly", false);

		temp.process(data, fr);
		Runtime.getRuntime().exec("lilypond " + fileName + ".ly");

	}
}
