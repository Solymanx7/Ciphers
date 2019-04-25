package com.example.android.ciphers;

import android.app.Activity;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;



public class MainActivity extends AppCompatActivity {
    private final String initializationVector = "INITALIZATION_VECTOR";

    /*private static final String UNICODE_FORMAT = "UTF8";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    byte [] keyAsBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;*/

    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    int clickCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Ciphers");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW


        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);





        final ImageButton swapIButton = (ImageButton) findViewById(R.id.Swap);
        final Button encryptButton = (Button) findViewById(R.id.Encrypt) ;
        final Button decryptButton = (Button) findViewById(R.id.Decrypt) ;
        final Button bruteforce = (Button) findViewById(R.id.BruteForce) ;
        final RadioGroup ciphersRadGro = (RadioGroup) findViewById(R.id.CiphersGroup);


        final EditText plainEText = (EditText)  findViewById(R.id.PlainText);
        final EditText keyCaeEText = (EditText)  findViewById(R.id.Key);
        final EditText keyDesEText = (EditText)  findViewById(R.id.Key1);
        final EditText resultEText = (EditText)  findViewById(R.id.Result);
        EditText temp;

        final ListView attacks = (ListView) findViewById(R.id.ListView);
        attacks.setAdapter(adapter);

        keyCaeEText.setEnabled(false);
        keyDesEText.setEnabled(false);

        ciphersRadGro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               keyCaeEText.setText(null);
               keyDesEText.setText(null);
               resultEText.setText(null);
               if(checkedId == R.id.TripleDESRadBut)
               {
                   keyDesEText.setVisibility(View.VISIBLE);
                   keyDesEText.setEnabled(true);

                   keyCaeEText.setVisibility(View.INVISIBLE);
                   keyCaeEText.setEnabled(false);
               }
               else if (checkedId == R.id.CaesarRadBut)
               {
                   keyCaeEText.setVisibility(View.VISIBLE);
                   keyCaeEText.setEnabled(true);

                   keyDesEText.setVisibility(View.INVISIBLE);
                   keyDesEText.setEnabled(false);
               }
            }
        });

        encryptButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                resultEText.setText(null);
                //if Caesar is selected
                if (ciphersRadGro.getCheckedRadioButtonId()== R.id.CaesarRadBut) {
                    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);
                    buttonClick.setDuration(300);
                    encryptButton.startAnimation(buttonClick);
                    EditText plainEText = (EditText) findViewById(R.id.PlainText);
                    EditText key = (EditText) findViewById(R.id.Key);
                    if ((TextUtils.isEmpty(plainEText.getText().toString())) || (TextUtils.isEmpty(keyCaeEText.getText().toString())))
                        return;
                    String keyString = key.getText().toString();
                    int keyValue = new Integer(keyString).intValue();
                    EncryptCaesar(plainEText.getText(), keyValue);

                }
                //if 3-DES is selected
                else if (ciphersRadGro.getCheckedRadioButtonId()== R.id.TripleDESRadBut)
                {
                        AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);
                        buttonClick.setDuration(300);
                        encryptButton.startAnimation(buttonClick);
                        EditText plainEText = (EditText) findViewById(R.id.PlainText);
                        EditText key = (EditText) findViewById(R.id.Key1);
                        String keyString = key.getText().toString();

                        /*if ((TextUtils.isEmpty(plainEText.getText().toString())) || (TextUtils.isEmpty(keyDesEText.getText().toString())))
                            return;*/

                    try {
                        EncryptDes(plainEText.getText().toString(), keyString);
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                resultEText.setText(null);
                //if Caesar is selected
                if (ciphersRadGro.getCheckedRadioButtonId()== R.id.CaesarRadBut) {
                    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);
                    buttonClick.setDuration(300);
                    decryptButton.startAnimation(buttonClick);
                    EditText plainEText = (EditText) findViewById(R.id.PlainText);
                    EditText key = (EditText) findViewById(R.id.Key);
                    if ((TextUtils.isEmpty(plainEText.getText().toString())) || (TextUtils.isEmpty(key.getText().toString())))
                        return;
                    String keyString = key.getText().toString();
                    int keyValue = new Integer(keyString).intValue();
                    DecryptCaesar(plainEText.getText(), keyValue);
                }
                //if 3-DES is selected
                else if (ciphersRadGro.getCheckedRadioButtonId()== R.id.TripleDESRadBut)
                {
                    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);
                    buttonClick.setDuration(300);
                    decryptButton.startAnimation(buttonClick);
                    EditText plainEText = (EditText) findViewById(R.id.PlainText);
                    EditText key = (EditText) findViewById(R.id.Key1);
                    String keyString = key.getText().toString();

                        /*if ((TextUtils.isEmpty(plainEText.getText().toString())) || (TextUtils.isEmpty(keyDesEText.getText().toString())))
                            return;*/

                    try {
                        DecryptDes(plainEText.getText().toString(), keyString);
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }

            }
        });

        swapIButton.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);
                buttonClick.setDuration(300);
                swapIButton.startAnimation(buttonClick);
                EditText plainEText = (EditText)  findViewById(R.id.PlainText);
                EditText resultEText = (EditText)  findViewById(R.id.Result);
                Editable temp;
                temp = plainEText.getText();
                plainEText.setText(resultEText.getText());
                resultEText.setText(null);
                //resultEText.setText(temp);

                Toast.makeText(getApplicationContext(), "Result and Plain are swapped now !", Toast.LENGTH_SHORT).show();

            }
        });

        bruteforce.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                if (ciphersRadGro.getCheckedRadioButtonId()== R.id.CaesarRadBut) {
                    listItems.clear();
                    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
                    buttonClick.setDuration(500);
                    bruteforce.startAnimation(buttonClick);
                    for (int i = 1; i < 26; i++) {
                        addItems(i, DecryptCaesar(plainEText.getText(), i));
                    }
                }
                else if (ciphersRadGro.getCheckedRadioButtonId()== R.id.TripleDESRadBut){
                    listItems.clear();
                    AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
                    buttonClick.setDuration(500);
                    bruteforce.startAnimation(buttonClick);
                    for (int i = 1; i < 21; i++) {

                        try {
                            addItems(i, DecryptDes(plainEText.getText().toString(), Integer.toString(i) ));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (NoSuchPaddingException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        plainEText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        keyCaeEText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        keyDesEText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        resultEText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


    }

    public void EncryptCaesar(Editable plain,int key)
    {

            char[] result = new char[plain.length()];
            for (int i = 0; i < result.length; i++) {
                if (plain.charAt(i) == ' ')
                    result[i] = ' ';
                else if (Character.isUpperCase(plain.charAt(i)))
                    result[i] = (char) (( (int) plain.charAt(i) + key - 65) % 26 + 65);
                else if (Character.isLowerCase(plain.charAt(i)))
                    result[i] = (char) (( (int) plain.charAt(i) + key - 97) % 26 + 97);
            }
            String resultSring = new String(result);
            EditText resultEText = (EditText) findViewById(R.id.Result);
            resultEText.setText(resultSring);
            Toast.makeText(getApplicationContext(), "Text is encrypted !", Toast.LENGTH_SHORT).show();
    }

    public String DecryptCaesar(Editable plain,int key)
    {
        char[] result = new char[plain.length()];
        for (int i = 0; i < result.length; i++) {
            if (plain.charAt(i) == ' ')
                result[i] = ' ';
            else if ( Character.isUpperCase(plain.charAt(i)) )
                result[i] = (char) (( (int) plain.charAt(i) - key - 90) % 26 + 90);
            else if ( Character.isLowerCase(plain.charAt(i)) )
                result[i] = (char) (( (int) plain.charAt(i) - key - 122) % 26 + 122);
        }
        String resultSring = new String(result);
        EditText resultEText = (EditText)  findViewById(R.id.Result);
        resultEText.setText(resultSring);
        return resultSring;

    }

    public void EncryptDes(String plain,String Key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        EditText resultEText = (EditText) findViewById(R.id.Result);


        byte[] KeyBytes = Arrays.copyOf(Key.getBytes(), 24);


        SecretKey secretKey = new SecretKeySpec(KeyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] plainTextBytes = plain.getBytes("utf-8");
        byte[] buf = cipher.doFinal(plainTextBytes);
        String encryptedString = Base64.encodeToString(buf,
                Base64.DEFAULT);

        resultEText.setText(encryptedString);




    }

    public String DecryptDes(String plain,String Key) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        EditText resultEText = (EditText) findViewById(R.id.Result);
        String decryptedString = "x-------Wrong Key-------x";

        try {
            byte[] message = Base64.decode(plain, Base64.DEFAULT);



        byte[] KeyBytes = Arrays.copyOf(Key.getBytes(), 24);
        SecretKey secretKey = new SecretKeySpec(KeyBytes, "DESede");
        Cipher decipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, secretKey);


        byte[] decrypted = decipher.doFinal(message);

        decryptedString = new String(decrypted,"UTF-8");
            resultEText.setText(decryptedString);
            return  decryptedString;
        }
        catch (Exception e){
            resultEText.setText(decryptedString);
            return  decryptedString;
        }


    }

    public void addItems(int Key,String trial ) {
        listItems.add("Key="+Key+"\n"+trial);
        adapter.notifyDataSetChanged();
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
