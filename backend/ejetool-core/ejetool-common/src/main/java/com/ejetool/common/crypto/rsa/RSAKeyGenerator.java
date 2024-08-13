package com.ejetool.common.crypto.rsa;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;
import java.util.Base64;


import com.ejetool.common.crypto.CryptoInitializeException;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class RSAKeyGenerator {

    private static final String PRIVATE_KEY_HEADER = "-----BEGIN PRIVATE KEY-----";
    private static final String PRIVATE_KEY_FOOTER = "-----END PRIVATE KEY-----";
    private static final String PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----";
    private static final String PUBLIC_KEY_FOOTER = "-----END PUBLIC KEY-----";

    public static KeyPair generate(){
        return generate(2048);
    }

    public static KeyPair generate(int keySize){
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(keySize); 
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("generate(): Notfound algorithm RSA.", e);
            throw new CryptoInitializeException("generate(): Notfound algorithm RSA.", e);
        }
    }

    public static KeyPair loadKeyPair(Path keyPath) {
        PrivateKey privateKey = loadPrivateKey(keyPath);
        PublicKey publicKey = generatePublicKey(privateKey);
        return new KeyPair(publicKey, privateKey);
    }

    public static PrivateKey loadPrivateKey(Path keyPath) {
        byte[] bytes;
        try {
            if(!keyPath.toFile().exists()){
                log.error("loadPrivateKey(): file not found. path=h="+keyPath.toAbsolutePath());
                throw new FileNotFoundException("file not found. path="+keyPath.toAbsolutePath());
            }
            bytes = Files.readAllBytes(keyPath);
        } catch (IOException e) {
            log.error("loadPrivateKey(): Failed read file. path="+keyPath, e);
            throw new CryptoInitializeException("Failed read file. path="+keyPath, e);
        }
        String content = new String(bytes);
        return createPrivateKey(content);
    }

    public static PrivateKey createPrivateKey(String content) {
        if (content.indexOf(PRIVATE_KEY_HEADER) < 0 || content.indexOf(PRIVATE_KEY_FOOTER) < 0){
            log.error("loadPrivateKey(): Invalid key header or footer.");
            throw new CryptoInitializeException("Invalid key header or footer.");
        }

        content = content.replace(PRIVATE_KEY_HEADER, "")
            .replace(PRIVATE_KEY_FOOTER, "")
            .replaceAll("\\s", "");
        
        byte[] encoded = Base64.getDecoder().decode(content);
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            log.error("loadPrivateKey(): Notfound algorithm RSA.", e);
            throw new CryptoInitializeException("Notfound algorithm RSA.", e);
        } catch (InvalidKeySpecException e) {
            log.error("loadPrivateKey(): Invalid key spec.", e);
            throw new CryptoInitializeException("Invalid key spec.", e);
        }
    }

    public static PublicKey loadPublicKey(Path keyPath) {
        byte[] bytes;
        try {
            if(!keyPath.toFile().exists()){
                log.error("loadPublicKey(): file not found. path="+keyPath.toAbsolutePath());
                throw new FileNotFoundException("file not found. path="+keyPath.toAbsolutePath());
            }
            bytes = Files.readAllBytes(keyPath);
        } catch (IOException e) {
            log.error("loadPublicKey(): Failed read file. path="+keyPath, e);
            throw new CryptoInitializeException("Failed read file. path="+keyPath, e);
        }
        
        String content = new String(bytes);
        return createPublicKey(content);
    }

    public static PublicKey createPublicKey(String content) {
        if (content.indexOf(PUBLIC_KEY_HEADER) < 0 || content.indexOf(PUBLIC_KEY_FOOTER) < 0){
            if (content.indexOf(PRIVATE_KEY_HEADER) > -1 && content.indexOf(PRIVATE_KEY_FOOTER) > -1){
                PrivateKey privateKey = createPrivateKey(content);
                return generatePublicKey(privateKey);
            }
            log.error("loadPublicKey(): Invalid key header or footer.");
            throw new CryptoInitializeException("Invalid key header or footer.");
        }
        
        content = content.replace(PUBLIC_KEY_HEADER, "")
            .replace(PUBLIC_KEY_FOOTER, "")
            .replaceAll("\\s", "");
        
        byte[] encoded = Base64.getDecoder().decode(content);
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            log.error("loadPublicKey(): Notfound algorithm RSA.", e);
            throw new CryptoInitializeException("Notfound algorithm RSA.", e);
        } catch (InvalidKeySpecException e) {
            log.error("loadPublicKey(): Invalid key spec.", e);
            throw new CryptoInitializeException("Invalid key spec.", e);
        }
    }

    public static PublicKey generatePublicKey(PrivateKey privateKey) {
        try {
            RSAPrivateCrtKey rsaPrivateKey = (RSAPrivateCrtKey)privateKey;
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPublicExponent());
            KeyFactory keyFactory = KeyFactory.getInstance(privateKey.getAlgorithm());
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            log.error("generatePublicKey(): Notfound algorithm RSA.", e);
            throw new CryptoInitializeException("Notfound algorithm RSA.", e);
        } catch (InvalidKeySpecException e) {
            log.error("generatePublicKey(): Invalid key spec.", e);
            throw new CryptoInitializeException("Invalid key spec.", e);
        }
    }

    public static void saveAs(KeyPair pair, String dirPath, String fileName){
        Path privateKeyFile = Path.of(dirPath, fileName+".key");
        Path publicKeyFile = Path.of(dirPath, fileName+".pub");
        saveAs(pair, privateKeyFile, publicKeyFile);
    }

    public static void saveAs(KeyPair pair, Path privateFile, Path publicFile){
        saveAs(pair, privateFile, publicFile, StandardOpenOption.CREATE_NEW);
    }

    public static void saveAs(KeyPair pair, Path privateFile, Path publicFile, StandardOpenOption oenOption){

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        saveAs("PRIVATE", privateKey.getEncoded(), privateFile, oenOption);
        try{
            saveAs("PUBLIC", publicKey.getEncoded(), publicFile, oenOption);
        } catch (Exception e){
            try {
                Files.delete(privateFile);
            } catch (IOException de) {
                log.warn("saveAs(): Failed to delete private key file.", de);
            }
        }
    }

    private static void saveAs(String keyType, byte[] key, Path file, StandardOpenOption oenOption){
        String encodedKey = Base64.getEncoder().encodeToString(key);

        StringBuilder contents = new StringBuilder();
        contents.append("-----BEGIN ").append(keyType).append(" KEY-----\n");
        contents.append(encodedKey.replaceAll("(.{64})", "$1\n"));
        contents.append("\n-----END ").append(keyType).append(" KEY-----");

        Path parent = file.getParent();
        if(parent != null && !Files.exists(parent)){
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                log.error("saveAs(): Failed to create directory. path=("+parent+")", e);
                throw new CryptoInitializeException("Failed to create directory. path=("+parent+")", e);
            }
        }
        try {
            var bytes = contents.toString().getBytes();
            Files.write(file, bytes, oenOption);
        } catch (IOException e) {
            log.error("saveAs(): Failed to write private key file. ("+keyType+")", e);
            throw new CryptoInitializeException("Failed to write private key file. ("+keyType+")", e);
        }
    }
}