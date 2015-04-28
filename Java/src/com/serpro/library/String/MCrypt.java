/*
 * 
 */
package com.serpro.library.String;

import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MCrypt {

        private String iv;
        private String SecretKey;
        
        private IvParameterSpec ivspec;
        private SecretKeySpec keyspec;
        private Cipher cipher;


        public MCrypt(String iv, String key)
        {
            try{

                this.setIv(iv);
                this.setSecretKey(key);

            } catch (Exception e) {

            }

            ivspec = new IvParameterSpec(iv.getBytes());

            keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

            try {
                    cipher = Cipher.getInstance("AES/CBC/NoPadding");
            } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
        }

        public String getIv(){
            return this.iv;
        }

        public IvParameterSpec getIvSpc(){
            return this.ivspec;
        }

        private void setIv(String iv) throws Exception {
            if(iv.length() != 16){
                throw new Exception("Quantidade IV inválida");
            }
            this.iv = iv;
        }

        private void setSecretKey(String key) throws Exception{
            if(key.length() != 16){
                throw new Exception("Quantidade Key Inválido");
            }
            this.SecretKey = key;
        }

        public static void main(String[] args) {
            
            try{

                MCrypt mcrypt = new MCrypt("9sb9510e375d44f0", "9756e93d09054s4f");  

                String encrypted = mcrypt.encrypt("佩德羅加布里埃爾 佩德羅加布里埃爾"); 
                String decrypted = mcrypt.decrypt(encrypted);

                System.out.println(encrypted);
                System.out.println(decrypted);

            }  catch(Exception e){

                System.out.println(e.getMessage());

            }
        }

        public String encrypt(String text) throws Exception
        {
            if(text == null || text.length() == 0)
                    throw new Exception("Empty string");

            byte[] encrypted = null;

            try {
                cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

                encrypted = cipher.doFinal(padString(text).getBytes());
            } catch (Exception e)
            {                       
                throw new Exception("[encrypt] " + e.getMessage());
            }

            return this.toBase64(encrypted);
        }

        public String decrypt(String code) throws Exception
        {
            if(code == null || code.length() == 0)
                    throw new Exception("Empty string");

            byte[] decrypted = null;

            try {
                    cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

                    decrypted = cipher.doFinal(Base64.decodeBase64(code));
                    //Remove trailing zeroes
                    if( decrypted.length > 0)
                    {
                        int trim = 0;
                        for( int i = decrypted.length - 1; i >= 0; i-- ) if( decrypted[i] == 0 ) trim++;

                        if( trim > 0 )
                        {
                            byte[] newArray = new byte[decrypted.length - trim];
                            System.arraycopy(decrypted, 0, newArray, 0, decrypted.length - trim);
                            decrypted = newArray;
                        }
                    }
            } catch (Exception e)
            {
                throw new Exception("[decrypt] " + e.getMessage());
            }

            return new String(decrypted);
        }      

        private static String padString(String source)
        {
            char paddingChar = 0;
            int size = 16;
            int x = source.length() % size;
            int padLength = size - x;

            for (int i = 0; i < padLength; i++)
            {
                  source += paddingChar;
            }

            return source;
        }

        public String toBase64(byte[] buffer){
            return Base64.encodeBase64String(buffer);
        }
}
