PGDMP  )    2                |            libros    14.12    16.3     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16394    libros    DATABASE     y   CREATE DATABASE libros WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Spain.1252';
    DROP DATABASE libros;
                postgres    false                        2615    2200    public    SCHEMA     2   -- *not* creating schema, since initdb creates it
 2   -- *not* dropping schema, since initdb creates it
                postgres    false            �           0    0    SCHEMA public    ACL     Q   REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;
                   postgres    false    4            �            1259    16396    libro    TABLE     �   CREATE TABLE public.libro (
    id integer NOT NULL,
    titulo text NOT NULL,
    autor character varying(255) NOT NULL,
    idioma character varying(50) NOT NULL,
    numero_de_descargas integer DEFAULT 0
);
    DROP TABLE public.libro;
       public         heap    postgres    false    4            �            1259    16395    libro_id_seq    SEQUENCE     �   CREATE SEQUENCE public.libro_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.libro_id_seq;
       public          postgres    false    210    4            �           0    0    libro_id_seq    SEQUENCE OWNED BY     =   ALTER SEQUENCE public.libro_id_seq OWNED BY public.libro.id;
          public          postgres    false    209            \           2604    16399    libro id    DEFAULT     d   ALTER TABLE ONLY public.libro ALTER COLUMN id SET DEFAULT nextval('public.libro_id_seq'::regclass);
 7   ALTER TABLE public.libro ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    209    210    210            �          0    16396    libro 
   TABLE DATA           O   COPY public.libro (id, titulo, autor, idioma, numero_de_descargas) FROM stdin;
    public          postgres    false    210   Z       �           0    0    libro_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.libro_id_seq', 50, true);
          public          postgres    false    209            _           2606    16404    libro libro_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.libro
    ADD CONSTRAINT libro_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.libro DROP CONSTRAINT libro_pkey;
       public            postgres    false    210            �     x����N�0���S�2�TEM���X(T���#�)�6��Fv�̼Rf�/�3uAݿ����F�S'_M�p˶'ݲ�g��kK#,�c�5;H�"��l
w�t���*+���j��k�8.zV�4�ad��vfX��3��έ�K�UC�&y!�9�\˖*it@k�ˁG����+r�����߃:���bH�6�	*�7j�F(1Z��I��V2$EI.��x��H_CE�����?�a��G�hC����P#+|hO�an/����LR�!~�j�f     