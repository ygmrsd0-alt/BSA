package bsa;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class BSA {

    static int diziBoyutu = 100;
    static int[] veri = new int[diziBoyutu];
    static int[] sol = new int[diziBoyutu];
    static int[] sag = new int[diziBoyutu];

    static boolean[] dolu = new boolean[diziBoyutu];
    static int rootIndeks = -1;

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < diziBoyutu; i++) {
            sol[i] = -1;
            sag[i] = -1;
            dolu[i] = false;
        }

        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File dosya = chooser.getSelectedFile();
        Scanner okuyucu = new Scanner(dosya);
        Random rand = new Random();

        while (okuyucu.hasNextInt()) {
            int sayi = okuyucu.nextInt();

            int rastgeleIndex;
            do {
                rastgeleIndex = rand.nextInt(diziBoyutu);
            } while (dolu[rastgeleIndex]);

            ekle(rastgeleIndex, sayi);
            dolu[rastgeleIndex] = true;
        }
        okuyucu.close();

        agacDurumunuYazdir();
        guncelBellekGoster();

        Scanner klavye = new Scanner(System.in);
        boolean devam = true;

        while (devam) {
            System.out.println("\n# İŞLEM MENÜSÜ #");
            System.out.println("1. Sayı Ara");
            System.out.println("2. Sayı Sil");
            System.out.println("3. Sayı Ekle");
            System.out.println("4. Bitir");
            System.out.print("Seçiminiz: ");

            int secim = klavye.nextInt();

            if (secim == 1) {
                System.out.print("Aranacak sayı: ");
                int aranan = klavye.nextInt();
                int sonuc = ara(aranan);
                if (sonuc != -1) {
                    System.out.println("Sayı bulundu. " + aranan + " sayısı dizinin " + sonuc + ". indeksinde.");
                } else {
                    System.out.println("Sayı ağaçta bulunamadı.");
                }

            } else if (secim == 2) {
                System.out.print("Silinecek sayı: ");
                int silinecek = klavye.nextInt();
                if (ara(silinecek) != -1) {
                    rootIndeks = sil(rootIndeks, silinecek);
                    System.out.println(silinecek + " silindi. Güncel ağaç yapısı:");
                    agacDurumunuYazdir();
                } else {
                    System.out.println("Sayı silinemedi, çünkü ağaçta yok.");
                }
                guncelBellekGoster();

            } else if (secim == 3) {
                
                System.out.print("Eklemek istediğiniz sayıyı girin: ");
                int yeniSayi = klavye.nextInt();
                
                if (ara(yeniSayi) != -1) {
                    System.out.println("Bu sayı zaten ağaçta mevcut!");
                } else {
                    
                    Random random = new Random();
                    int rIdx;
                    do {
                        rIdx = rand.nextInt(diziBoyutu);
                    } while (dolu[rIdx]);

                    dolu[rIdx] = true;
                    veri[rIdx] = yeniSayi;
                    sol[rIdx] = -1; 
                    sag[rIdx] = -1;

                    if (rootIndeks == -1) {
                        rootIndeks = rIdx; 
                    } else {
                        ekle(rIdx, yeniSayi);
                    }

                    System.out.println(yeniSayi + " sayısı başarıyla " + rIdx + ". indekse eklendi.");
                    agacDurumunuYazdir();
                    guncelBellekGoster();
                }
            } else if (secim == 4) {
                System.out.println("Program sonlandırılıyor...");
                devam = false;
            } else {
                System.out.println("Geçersiz seçim!");
            }
        }

    }

    public static void ekle(int yeniIndeks, int sayi) {
        veri[yeniIndeks] = sayi;

        if (rootIndeks == -1) {
            rootIndeks = yeniIndeks;
            return;
        }

        int suanki = rootIndeks;
        while (true) {
            if (sayi < veri[suanki]) {
                if (sol[suanki] == -1) {
                    sol[suanki] = yeniIndeks;
                    break;
                }
                suanki = sol[suanki];
            } else {
                if (sag[suanki] == -1) {
                    sag[suanki] = yeniIndeks;
                    break;
                }
                suanki = sag[suanki];
            }
        }
    }

    public static int ara(int sayi) {
        int suanki = rootIndeks;
        while (suanki != -1) {
            if (sayi == veri[suanki]) {
                return suanki;
            } else if (sayi < veri[suanki]) {
                suanki = sol[suanki];
            } else {
                suanki = sag[suanki];
            }
        }
        return -1;
    }

    public static int sil(int root, int sayi) {
        if (root == -1) {
            return -1;
        }

        if (sayi < veri[root]) {
            sol[root] = sil(sol[root], sayi);
        } else if (sayi > veri[root]) {
            sag[root] = sil(sag[root], sayi);
        } else {
            dolu[root] = false;
            int silinenVeri = veri[root];
            veri[root] = 0;

            if (sol[root] == -1) {
                return sag[root];
            }
            if (sag[root] == -1) {
                return sol[root];
            }

            int minIndeks = enKucukBul(sag[root]);
            veri[root] = veri[minIndeks];
            sag[root] = sil(sag[root], veri[minIndeks]);
        }
        return root;
    }

    private static int enKucukBul(int root) {
        int suanki = root;
        while (sol[suanki] != -1) {
            suanki = sol[suanki];
        }
        return suanki;
    }

    public static void agacDurumunuYazdir() {
        System.out.print("\nPreOrder : ");
        preOrder(rootIndeks);
        System.out.print("\nInOrder : ");
        inOrder(rootIndeks);
        System.out.print("\nPostOrder : ");
        postOrder(rootIndeks);
        System.out.println();
    }

    public static void guncelBellekGoster() {
        System.out.println("\n# Güncel Dizi Bellek Durumu #");
        for (int i = 0; i < diziBoyutu; i++) {
            if (dolu[i]) {
                System.out.println("İndeks [" + i + "] = Veri: " + veri[i] + " , Sol: " + sol[i] + " , Sağ: " + sag[i]);
            }
        }
    }

    public static void inOrder(int indeks) {
        if (indeks == -1) {
            return;
        }
        inOrder(sol[indeks]);
        System.out.print(veri[indeks] + " ");
        inOrder(sag[indeks]);
    }

    public static void preOrder(int indeks) {
        if (indeks == -1) {
            return;
        }
        System.out.print(veri[indeks] + " ");
        preOrder(sol[indeks]);
        preOrder(sag[indeks]);
    }

    public static void postOrder(int indeks) {
        if (indeks == -1) {
            return;
        }
        postOrder(sol[indeks]);
        postOrder(sag[indeks]);
        System.out.print(veri[indeks] + " ");
    }
}
