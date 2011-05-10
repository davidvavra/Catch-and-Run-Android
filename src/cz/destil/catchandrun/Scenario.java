package cz.destil.catchandrun;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import android.location.Location;
import android.os.Handler;
import android.os.Message;

import com.google.android.maps.GeoPoint;

public class Scenario {

	private String myLocationMovement = "[1],14.391403,50.102689;[2],14.392133,50.102386;[3],14.392862,50.101974;[4],14.393463,50.102304;[5],14.394107,50.102469;[6],14.394708,50.102662;[7],14.395437,50.102607;[8],14.396124,50.102662;[9],14.396896,50.102799;[10],14.396853,50.10313;[11],14.397454,50.103295;[12],14.397969,50.103295;[13],14.398742,50.103295;[14],14.399214,50.103543;[15],14.399858,50.103653;[16],14.400458,50.103845;[17],14.401016,50.103983;[18],14.401703,50.104066;[19],14.402304,50.104286;[20],14.402947,50.104506;[21],14.403334,50.104093;[22],14.403634,50.103515;[22],14.403634,50.103515;[22],14.403634,50.103515;[22],14.403634,50.103515;[22],14.403634,50.103515;[23],14.404364,50.103322;[24],14.405308,50.10324;[25],14.406166,50.103212;[26],14.40681,50.10335;[27],14.407325,50.103543;[28],14.408011,50.10368;[29],14.408655,50.10379;[30],14.40917,50.103955;[31],14.409471,50.104423;[32],14.409771,50.104864;[33],14.410415,50.105167;[34],14.410887,50.105552;[35],14.411316,50.105992;[36],14.411488,50.106433;[37],14.412131,50.10679;[38],14.412732,50.106901;[39],14.413548,50.107093;[40],14.414492,50.107286;[41],14.415178,50.107451;[42],14.415693,50.107011;[43],14.416466,50.107038;[44],14.417067,50.107286;[45],14.417753,50.107424;[46],14.418526,50.107313;[47],14.419255,50.107258;[48],14.420199,50.107368;[49],14.420757,50.107424;[50],14.421401,50.107479;[51],14.422216,50.107176;[52],14.422388,50.106625;[53],14.422603,50.106047;[54],14.422903,50.105497;[55],14.422002,50.104974;[55],14.422002,50.104974;[55],14.422002,50.104974;[55],14.422002,50.104974;[55],14.422002,50.104974;[120],14.421852,50.104355;[121],14.421465,50.104121;[122],14.420285,50.103955;[123],14.418526,50.10401;[124],14.416895,50.104121;[125],14.415178,50.104382;[126],14.413548,50.104258;[127],14.412796,50.103873;[128],14.412324,50.103446;[129],14.411273,50.102937;[130],14.410543,50.102799;[131],14.409492,50.102593;[132],14.407604,50.102359;[133],14.407218,50.101863;[134],14.40711,50.101313;[135],14.407067,50.100817";
	private String playerLocationMovement = "[56],14.402947,50.101175;[57],14.402304,50.101313;[58],14.401617,50.101506;[59],14.401016,50.101781;[60],14.400501,50.102194;[61],14.400201,50.102607;[62],14.399557,50.102441;[63],14.39887,50.102304;[64],14.399171,50.101808;[65],14.399557,50.101203;[66],14.400072,50.101506;[67],14.401274,50.102139;[68],14.402003,50.102249;[69],14.402347,50.102689;[111],14.402733,50.1021391;[112],14.402947,50.1016711;[113],14.403806,50.1010651;[114],14.404922,50.1016431;[115],14.403934,50.102249;[70],14.402046,50.103102;[71],14.402647,50.103267;[72],14.403119,50.103405;[72],14.403119,50.103405;[72],14.403119,50.103405;[72],14.403119,50.103405;[72],14.403119,50.103405;[73],14.404235,50.103845;[74],14.404664,50.104258;[75],14.405093,50.104644;[117],14.406724,50.104148;[118],14.405909,50.104286;[78],14.406681,50.104644;[79],14.407067,50.104974;[80],14.407711,50.105084;[81],14.40887,50.104836;[82],14.408226,50.104974;[119],14.409342,50.10474;[83],14.410458,50.104644;[84],14.411101,50.104974;[85],14.41153,50.105387;[86],14.411831,50.105855;[87],14.412131,50.106268;[88],14.412861,50.106433;[89],14.41299,50.107286;[90],14.413891,50.107451;[91],14.414663,50.107726;[92],14.415565,50.107836;[93],14.416294,50.108222;[94],14.416852,50.108332;[95],14.417882,50.108524;[96],14.41844,50.10891;[97],14.419169,50.108992;[98],14.419684,50.109102;[99],14.4205,50.109102;[100],14.421186,50.10913;[101],14.421916,50.10913;[102],14.422774,50.10913;[103],14.423289,50.10891;[104],14.424105,50.1088;[105],14.424663,50.108332;[106],14.42462,50.107781;[107],14.424577,50.107258;[108],14.424405,50.106763;[109],14.424362,50.106212;[110],14.424233,50.105634;[136],14.42477,50.105552;[137],14.425607,50.105483;[138],14.426422,50.105401;[139],14.427388,50.105841;[140],14.427495,50.106295;[141],14.427023,50.106667;[142],14.426057,50.106818;[143],14.427023,50.107093;[144],14.428589,50.107382;[145],14.429255,50.107547;[146],14.430671,50.107891;[147],14.431207,50.107795;[148],14.431572,50.106928;[149],14.43213,50.106185;[150],14.432859,50.10569;[151],14.433353,50.105139;[152],14.432495,50.104547";
	private String bordersCoords = "[1],14.395094,50.106199;[2],14.396574,50.106419;[3],14.401295,50.10763;[4],14.402883,50.108002;[5],14.405501,50.1085;[6],14.406595,50.1089;[7],14.409471,50.1090;[8],14.411638,50.109158;[9],14.418569,50.109158;[10],14.422946,50.109763;[11],14.425392,50.109722;[12],14.427001,50.108153;[13],14.42786,50.10635;[14],14.428439,50.105703;[15],14.430048,50.105951;[16],14.431572,50.103983;[17],14.428718,50.10357;[18],14.423289,50.103584;[19],14.41962,50.103694;[20],14.417818,50.103198;[21],14.41653,50.1024;[22],14.414642,50.102593;[23],14.41153,50.102758;[24],14.408033,50.100102;[25],14.4052,50.099317;[26],14.404106,50.100088;[27],14.400609,50.100611;[28],14.397519,50.100611;[29],14.395351,50.101492;[30],14.395072,50.104286";
	private List<Location> myLocations;
	private List<GeoPoint> playerLocations;
	private int time = 0;
	private GameActivity context;
	private static final int UPDATE_INTERVAL = 3000;
	private Player otherPlayer;
	private Treasure treasure;
	private CurrentLocationOverlay myPlayer;
	private GameEvents events;
	final Timer timer = new Timer();

	public Scenario(GameActivity context) {
		this.context = context;
		myPlayer = context.myLocation;
		events = new GameEvents(context);

		// parse borders
		ArrayList<GeoPoint> borders = new ArrayList<GeoPoint>();
		StringTokenizer tokenizer = new StringTokenizer(bordersCoords, ";");
		while (tokenizer.hasMoreTokens()) {
			StringTokenizer tokenizer2 = new StringTokenizer(
					tokenizer.nextToken(), ",");
			tokenizer2.nextToken();
			int lon = (int) (Double.parseDouble(tokenizer2.nextToken()) * 1e6);
			int lat = (int) (Double.parseDouble(tokenizer2.nextToken()) * 1e6);
			GeoPoint point = new GeoPoint(lat, lon);
			borders.add(point);
		}
		context.border.setGeoPoints(borders);

		// setup my player
		myPlayer.setName("You");
		myPlayer.setHiddenTime(11*(UPDATE_INTERVAL/1000));
		myPlayer.role = Common.OUTSIDE_AREA;
		myPlayer.money = 33;
		// setup other player
		otherPlayer = new Player(new GeoPoint((int) (49 * 1e6),
				(int) (15 * 1e6)), Common.IDLER, "Honza123", 22, false);
		context.players.addPlayer(otherPlayer);
		// setup treasure
		treasure = new Treasure(new GeoPoint((int) (50.104974 * 1e6),
				(int) (14.422002 * 1e6)), 100);
		this.context.treasures.addTreasure(treasure);

		// parse my locations
		myLocations = new ArrayList<Location>();
		tokenizer = new StringTokenizer(myLocationMovement, ";");
		while (tokenizer.hasMoreTokens()) {
			StringTokenizer tokenizer2 = new StringTokenizer(
					tokenizer.nextToken(), ",");
			tokenizer2.nextToken();
			Location location = new Location("mockgps");
			location.setLongitude(Double.parseDouble(tokenizer2.nextToken()));
			location.setLatitude(Double.parseDouble(tokenizer2.nextToken()));
			myLocations.add(location);
		}

		// parse player locations
		playerLocations = new ArrayList<GeoPoint>();
		tokenizer = new StringTokenizer(playerLocationMovement, ";");
		while (tokenizer.hasMoreTokens()) {
			StringTokenizer tokenizer2 = new StringTokenizer(
					tokenizer.nextToken(), ",");
			tokenizer2.nextToken();
			int lon = (int) (Double.parseDouble(tokenizer2.nextToken()) * 1e6);
			int lat = (int) (Double.parseDouble(tokenizer2.nextToken()) * 1e6);
			GeoPoint point = new GeoPoint(lat, lon);
			playerLocations.add(point);
		}
	}

	public void start() {
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (!update())
					timer.cancel();
			}
		};
		TimerTask timerTask = new TimerTask() {
			public void run() {
				handler.sendEmptyMessage(0);
			}
		};
		timer.schedule(timerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);

	}

	private boolean update() {
		if (time == myLocations.size())
			return false;
		myPlayer.onLocationChanged(myLocations.get(time));
		context.players.changeLocation(otherPlayer, playerLocations.get(time));
		otherPlayer = context.players.getItem(0);

		// time based events
		switch (time) {
		case 7:
			myPlayer.role = Common.IDLER;
			break;
		case 9:
			events.go(GameEvents.BECAME_CATCHER);
			break;
		case 21:
			events.go(GameEvents.WON);
			break;
		case 29:
			events.go(GameEvents.BECAME_RUNNER);
			break;
		case 36:
		case 41:
		case 42:
		case 44:
			//possible loose
			if (myPlayer.role == Common.RUNNER)
			{
				events.go(GameEvents.LOST);
			}
			break;
		case 51:
			//possible win
			if (myPlayer.role == Common.RUNNER)
			{
				events.go(GameEvents.WON);
			}
			break;
		case 58:
			//found treasure
			events.go(GameEvents.TREASURE_FOUND);
			break;
		}

		// role-based behaviour
		switch (myPlayer.role) {
		case Common.HIDDEN:
			myPlayer.decreaseHiddenTime(UPDATE_INTERVAL/1000);
			break;
		case Common.IDLER:
			myPlayer.money++;
			break;
		}

		myPlayer.update();
		time++;
		return true;
	}
}
