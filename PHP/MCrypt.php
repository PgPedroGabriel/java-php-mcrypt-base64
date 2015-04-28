<?php
class Encryption
{
    const CIPHER = MCRYPT_RIJNDAEL_128; // Rijndael-128 is AES
    const MODE   = MCRYPT_MODE_CBC;

    /* Cryptographic key of length 16, 24 or 32. NOT a password! */
    private $key = "9756e93d09054s4f";
    private $iv = "9sb9510e375d44f0";

    public function __construct($iv = null, $key = null) {
        if($key != null){
            $this->key = $key;
        }

        if($iv != null){
            $this->iv = $iv;
        }
        return $this;
    }

    public function encrypt($plaintext) {
        if($this->iv == null){
            $ivSize = mcrypt_get_iv_size(self::CIPHER, self::MODE);
            $this->iv = mcrypt_create_iv($ivSize, MCRYPT_DEV_RANDOM);
        }
        $ciphertext = mcrypt_encrypt(self::CIPHER, $this->key, $plaintext, self::MODE, $this->iv);
        return base64_encode($iv.$ciphertext);
    }

    public function decrypt($ciphertext) {
        $ciphertext = base64_decode($ciphertext);
        
        if($this->iv == null){
            
            $ivSize = mcrypt_get_iv_size(self::CIPHER, self::MODE);
            if (strlen($ciphertext) < $ivSize) {
                return null;
            }
            $this->iv = substr($ciphertext, 0, $ivSize);
            $ciphertext = substr($ciphertext, $ivSize);

        }
        $plaintext = mcrypt_decrypt(self::CIPHER, $this->key, $ciphertext, self::MODE, $this->iv);
        return rtrim($plaintext, "\0");
    }
}