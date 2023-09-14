package com.ssafy.triplet.daily.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.triplet.daily.repository.DailyRepository;
import com.ssafy.triplet.exception.BaseException;
import com.ssafy.triplet.exception.ErrorCode;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class S3Service {
	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String S3Bucket; // Bucket 이름

	@Value("${profile-path}")
	private String imageDir; // 저장할 폴더 이름

	private final DailyRepository dailyRepository;

	public String uploadImage(MultipartFile multipartFile) throws IOException {
		verifyFileType(multipartFile.getContentType());
		log.info(multipartFile.getOriginalFilename());
		String fileName = createFileName(multipartFile.getOriginalFilename());

		ObjectMetadata objectMetaData = new ObjectMetadata();
		objectMetaData.setContentType(multipartFile.getContentType());
		objectMetaData.setContentLength(multipartFile.getSize());

		try {
			amazonS3Client.putObject(
				new PutObjectRequest(S3Bucket, fileName, multipartFile.getInputStream(), objectMetaData)
				//  .withCannedAcl(CannedAccessControlList.PublicRead)
			);
		} catch (IOException e) {
			throw new IOException();
		}

		return amazonS3Client.getUrl(S3Bucket, fileName).toString();

	}

	public void deleteImage(String url) {
		try {
			String key = url.substring(url.indexOf("profile"));
			amazonS3Client.deleteObject(new DeleteObjectRequest(S3Bucket, key));
		} catch (Exception e) {
			log.info("삭제할 파일이 존재하지 않습니다");
		}
	}

	public String getImageUrl(Long dailyId) {
		return dailyRepository.findById(dailyId)
			.orElseThrow(() -> new BaseException(ErrorCode.DAILY_ID_NOT_FOUND))
			.getImageUrl();
	}

	public String createFileName(String originalFileName) {
		String type = originalFileName.substring(originalFileName.lastIndexOf("."));
		return imageDir + "/" + UUID.randomUUID().toString().concat(type);
	}

	public void verifyFileType(String type) {
		String[] typeList = {"image/jpeg", "image/jpg", "image/png"};
		List<String> strList = new ArrayList<>(Arrays.asList(typeList));
		if (!strList.contains(type))
			throw new BaseException(ErrorCode.IMAGE_TYPE_ERROR);
	}
}
