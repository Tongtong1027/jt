package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVO;

@Service
@PropertySource("classpath:/properties/image.properties")
//@ConfigurationProperties(prefix="image")
public class FileServiceImpl implements FileService {
	//定义本地磁盘路径
	@Value("${image.localDirPath}")
	private String localDirPath;
	//定义虚拟路径名称
	@Value("${image.urlPath}")
	private String urlPath;

	/**1.获取图片的名称
	 * 2.校验图片的类型  jpg|png|gif
	 * 3.校验是否为恶意程序 木马.exe.jpg
	 * 4.分文件保存 按照时间存储 yyyy/MM/dd
	 * 5.防止文件重名.UUID 32位16进制数+毫秒数
	 *
	 *正则常用字符：
	 *1. ^ 标识以...开始字符
	 *2. $ 以...结束
	 *3.    点.  任意单个字符
	 *4. * 表示任意个0~无穷
	 *5. + 表示1~无穷
	 *6. .\. 表示特殊字符点
	 *7. (XX|XX|XX) 代表分组，满足其中一个即可
	 */
	//从用户磁盘上传到服务器磁盘中
	@Override
	public ImageVO uploadFile(MultipartFile uploadFile) {
		ImageVO imageVO = new ImageVO();
		//1.获取文件的名称
		String fileName = uploadFile.getOriginalFilename();
		fileName = fileName.toLowerCase();
		//2.校验图片类型,使用正则表达式判断字符串 a.jpg.aaa
		if(!fileName.matches("^.+\\.(jpg|png|gif)$")) {
			imageVO.setError(1);//表示上传有误
			return imageVO;
		}
		//3.判断是否为恶意程序 图片模板 
		try {
			BufferedImage bufferedImage = 
					ImageIO.read(uploadFile.getInputStream());
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			
			if(width==0||height==0) {
				imageVO.setError(1);
				return imageVO;
			}
			//4.时间转化为字符串
			String dateDir = 
					new SimpleDateFormat("yyyy/MM/dd")
					.format(new Date());
			
			//5.准备文件夹
			String localDir = localDirPath + dateDir;
			
			File dirFire = new File(localDir);
			if(!dirFire.exists()) {
				//如果文件不存在,则创建文件夹
				dirFire.mkdirs();
			}
			//f515fd465465-ba45df746-855-11fdeff-13513sdf15165
			//6.使用UUID定义文件名称uuid.jpg
			String uuid = UUID.randomUUID().toString()
					.replace("-", "");
			//图片类型  a.jpg  动态获取".jpg"
			String fileType = 
					fileName.substring(fileName.lastIndexOf("."));
			
			//拼接新文件名称
			//E:/jt/image/yyyy/MM/dd/文件名称.类型
			String realLocalPath = localDir + "/" + uuid + fileType;
			
			//7完成文件的上传
			uploadFile.transferTo(new File(realLocalPath));
			
			//8.拼接url路径  http://image.jt.com/yyyy/MM/dd/uuid/fileType
			String realUrlPath = urlPath+dateDir+"/"+uuid+fileType;
			//将文件信息回传给页面
			imageVO.setError(0)
				   .setUrl(realUrlPath)
				   .setHeight(height)
				   .setWidth(width);
					
			return imageVO;
		} catch (Exception e) {
			e.printStackTrace();
			imageVO.setError(1);
			return imageVO;
		}
		
			 	
	}

}
