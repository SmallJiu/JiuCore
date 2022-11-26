package cat.jiu.core.util.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * @see {@linkplain https://blog.csdn.net/weixin_35285276/article/details/114118964}
 * @author small_jiu
 */
public enum GoogleTranslate {
	zh_CN("zh_CN"),
	en_US("en"),
	zh_tw("zh_TW"),
	af_za("af"),
	ar_sa("ar"),
	ast_es("es"),
	az_az("az"),
	be_by("be"),
	bg_bg("bg"),
	br_fr("br"),
	bs_ba("bs"),
	ca_es("ca"),
	cs_cz("cs"),
	cy_gb("cy"),
	da_dk("da"),
	de_alg("de"),
	de_at("at"),
	de_ch("ch"),
	de_de("en"),
	el_gr("gl"),
	en_au("au"),
	en_ca("en"),
	en_gb("en"),
	en_nz("en"),
	en_pt("en"),
	en_ud("en"),
	en_ws("en"),
	eo_uy("en"),
	es_ar("en"),
	es_cl("en"),
	es_es("en"),
	es_mx("en"),
	es_uy("en"),
	es_ve("en"),
	et_ee("en"),
	eu_es("eu"),
	fa_ir("en"),
	fi_fi("en"),
	fil_ph("en"),
	fo_fo("en"),
	fr_ca("en"),
	fy_nl("en"),
	ga_ie("en"),
	gd_gb("en"),
	gl_es("en"),
	gv_im("en"),
	haw_us("en"),
	he_il("en"),
	hi_in("en"),
	hr_hr("en"),
	hu_hu("en"),
	hy_am("hy"),
	id_id("en"),
	ig_ng("en"),
	io_en("en"),
	is_is("en"),
	it_it("en"),
	ja_jp("en"),
	jbo_en("en"),
	ka_ge("en"),
	kab_kab("en"),
	kn_in("en"),
	ko_kr("en"),
	ksh_de("en"),
	kw_gb("en"),
	la_la("en"),
	lb_lu("en"),
	li_li("en"),
	lol_us("en"),
	lt_lt("en"),
	lv_lv("en"),
	mi_nz("en"),
	mk_mk("en"),
	mn_mn("en"),
	ms_my("en"),
	mt_mt("en"),
	nds_de("en"),
	nl_be("en"),
	nn_no("en"),
	no_no("en"),
	oc_fr("en"),
	oj_ca("en"),
	pl_pl("en"),
	pt_br("en"),
	pt_pt("en"),
	qya_aa("en"),
	ro_ro("en"),
	ru_ru("en"),
	se_no("en"),
	sk_sk("en"),
	sl_si("en"),
	so_so("en"),
	sq_al("sq"),
	sr_sp("en"),
	sv_se("en"),
	ta_in("en"),
	th_th("en"),
	tlh_aa("en"),
	tr_tr("en"),
	tzl_tzl("en"),
	uk_ua("en"),
	val_es("en"),
	vec_it("en"),
	vi_vn("en"),
	yo_ng("en")
	
	;
	static final JsonParser parser = new JsonParser();
	private final String code;
	private GoogleTranslate(String code) {
		this.code = code;
	}
	
	public String translate(GoogleTranslate to, String text) throws IOException {
		if(this==to) return text;
		HttpURLConnection con = (HttpURLConnection) new URL(
				"https://translate.googleapis.com/translate_a/single?" +
				"client=gtx&" +
				"sl=" + this.code +
				"&tl=" + to.code +
				"&dt=t&q=" + URLEncoder.encode(text, "UTF-8")).openConnection();
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return parseResult(response.toString());
	}
	private static String parseResult(String inputJson) {
		JsonArray jsonArray2 = parser.parse(inputJson).getAsJsonArray().get(0).getAsJsonArray();

		String result = "";
		for(int i = 0; i < jsonArray2.size(); i++) {
			result += jsonArray2.get(i).getAsJsonArray().get(0).toString();
		}
		return result;
	}
}
