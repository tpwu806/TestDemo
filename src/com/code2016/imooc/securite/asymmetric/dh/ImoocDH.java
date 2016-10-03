package com.code2016.imooc.securite.asymmetric.dh;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

import org.apache.commons.codec.binary.Base64;
public class ImoocDH {
	
	private static String src = "imooc security dh";

	public static void main(String[] args) {
		jdkDH();
	}
	
	public static void jdkDH(){
		
		try {
			//1初始化发送方密钥
			KeyPairGenerator senderKeypairGenerator = KeyPairGenerator.getInstance("DH");
			senderKeypairGenerator.initialize(512);
			KeyPair senderKeyPair = senderKeypairGenerator.generateKeyPair();
			byte[] senderPiblicKeyEnc = senderKeyPair.getPublic().getEncoded();//发送方公钥，发送给接收方（网络、文件）
			
			
			//2.初始化接受方密钥		
			KeyFactory receiverKeyFactory = KeyFactory.getInstance("DH");
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(senderPiblicKeyEnc);
			PublicKey receiverPublicKey = receiverKeyFactory.generatePublic(x509EncodedKeySpec);
			DHParameterSpec	dhParameterSpec	= ((DHPublicKey)receiverPublicKey).getParams();
			KeyPairGenerator receiverKeypairGenerator = KeyPairGenerator.getInstance("DH");
			receiverKeypairGenerator.initialize(dhParameterSpec);
			KeyPair receiverKeyPair = receiverKeypairGenerator.generateKeyPair();
			PrivateKey receiverPrivateKey = receiverKeyPair.getPrivate();
			byte[] receiverPublicKeyEnc = receiverKeyPair .getPublic().getEncoded();
			
			//3.密钥构建	
			KeyAgreement receiverkeyAgreement = KeyAgreement.getInstance("DH");
			receiverkeyAgreement.init(receiverPrivateKey);
			receiverkeyAgreement.doPhase(receiverPublicKey, true);			
			SecretKey receiverDesKey = receiverkeyAgreement.generateSecret("DES");
			
			KeyFactory senderKeyFactory = KeyFactory.getInstance("DH");
			x509EncodedKeySpec = new X509EncodedKeySpec(receiverPublicKeyEnc);
			PublicKey senderPublicKey = senderKeyFactory.generatePublic(x509EncodedKeySpec);
			KeyAgreement senderKeyAgreement = KeyAgreement.getInstance("DH");
			senderKeyAgreement.init(senderKeyPair.getPrivate());
			senderKeyAgreement.doPhase(senderPublicKey, true);
			
			SecretKey senderDesKey = senderKeyAgreement.generateSecret("DES");
			if(Objects.equals(receiverDesKey,senderDesKey))
				System.out.println("双方密钥相同！");
			
			//4.加密
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, senderDesKey);
			byte[] result = cipher.doFinal(src.getBytes());
			System.out.println("jdk dh encrypt: " + Base64.encodeBase64String(result));
			
			//解密
			cipher.init(Cipher.DECRYPT_MODE, receiverDesKey);
			result = cipher.doFinal(result);
			System.out.println("jdk dh decrypt: " + new String(result));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
