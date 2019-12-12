package com.optimizer.optimize;

import com.google.common.collect.Sets;
import optimize.Optimizer;
import optimize.Player;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class OptimizerTest {
    private List<Player> playerList = new ArrayList<>();
    private Optimizer optimizer;
    private Optimizer optimizerWithWhiteList;
    private List<Player> testWhiteList = Collections.singletonList(new Player(456613));
    private List<String> lineupMatrix = new LinkedList<>(Arrays.asList("QB", "RB", "RB", "WR", "WR", "WR", "TE", "RB,WR,TE", "D"));

    OptimizerTest() {
        String playerListString = "[{position=RB, projection=24.45617861968624, salary=11000, playerId=830517}, {position=RB, projection=17.723918987283547, salary=9100, playerId=732145}, {position=QB, projection=22.26028086187252, salary=9000, playerId=877745}, {position=RB, projection=16.472570383710003, salary=8700, playerId=824080}, {position=WR, projection=18.496090280395002, salary=8600, playerId=653699}, {position=QB, projection=19.378388677148134, salary=8400, playerId=213957}, {position=WR, projection=15.8326079658086, salary=8400, playerId=611417}, {position=QB, projection=20.918017657989424, salary=8400, playerId=839031}, {position=WR, projection=12.153792747064857, salary=8300, playerId=560241}, {position=WR, projection=14.390466032061063, salary=8300, playerId=823156}, {position=QB, projection=19.95284065852438, salary=8200, playerId=403308}, {position=WR, projection=15.937734102200539, salary=8000, playerId=694041}, {position=RB, projection=15.685799766900708, salary=7900, playerId=822857}, {position=WR, projection=14.672963747895817, salary=7900, playerId=835127}, {position=QB, projection=17.28165351688948, salary=7800, playerId=868199}, {position=WR, projection=15.175322317950984, salary=7800, playerId=456614}, {position=RB, projection=13.941954420908843, salary=7800, playerId=741314}, {position=QB, projection=14.70748454982494, salary=7800, playerId=555358}, {position=QB, projection=16.462009099458456, salary=7700, playerId=25598}, {position=QB, projection=18.985664430081624, salary=7700, playerId=25347}, {position=QB, projection=16.401414605561495, salary=7700, playerId=880026}, {position=QB, projection=20.81261723599929, salary=7700, playerId=828743}, {position=WR, projection=15.828818416179898, salary=7700, playerId=593587}, {position=QB, projection=20.54501982808947, salary=7700, playerId=691536}, {position=RB, projection=13.240562094724595, salary=7700, playerId=944416}, {position=RB, projection=17.032121072785475, salary=7600, playerId=750846}, {position=QB, projection=17.304786180267843, salary=7600, playerId=748070}, {position=QB, projection=20.299425788204715, salary=7600, playerId=216263}, {position=WR, projection=14.855819009688068, salary=7600, playerId=334733}, {position=QB, projection=17.048011434044778, salary=7500, playerId=879799}, {position=RB, projection=16.71217316585599, salary=7500, playerId=822013}, {position=RB, projection=12.525118633183299, salary=7400, playerId=456613}, {position=WR, projection=9.549288687021633, salary=7400, playerId=697295}, {position=QB, projection=17.154966383148466, salary=7400, playerId=607047}, {position=QB, projection=18.344224706270417, salary=7400, playerId=161355}, {position=RB, projection=10.21626344375163, salary=7300, playerId=840760}, {position=QB, projection=15.955088070727307, salary=7300, playerId=246054}, {position=WR, projection=13.16861774231318, salary=7300, playerId=838878}, {position=QB, projection=17.088362107846088, salary=7300, playerId=380960}, {position=RB, projection=13.481748644020126, salary=7300, playerId=606516}, {position=WR, projection=13.708638196418876, salary=7200, playerId=602241}, {position=WR, projection=14.596269049369264, salary=7200, playerId=589991}, {position=QB, projection=18.96046218919691, salary=7100, playerId=822350}, {position=WR, projection=17.349894896782526, salary=7100, playerId=877790}, {position=TE, projection=13.588371113889947, salary=7100, playerId=448240}, {position=QB, projection=15.517187952984136, salary=7000, playerId=496083}, {position=QB, projection=14.29097336383242, salary=7000, playerId=322858}, {position=RB, projection=10.915183136576443, salary=7000, playerId=790004}, {position=QB, projection=14.868911331463375, salary=7000, playerId=837040}, {position=QB, projection=11.644233546735514, salary=6900, playerId=821297}, {position=WR, projection=14.369484152765226, salary=6900, playerId=884013}, {position=WR, projection=13.582954094677158, salary=6800, playerId=589984}, {position=QB, projection=15.377729936637868, salary=6800, playerId=882345}, {position=WR, projection=13.267816455735462, salary=6800, playerId=557210}, {position=RB, projection=12.061040241856972, salary=6700, playerId=820727}, {position=WR, projection=8.299735334482076, salary=6700, playerId=835437}, {position=RB, projection=9.982349180661368, salary=6700, playerId=916466}, {position=TE, projection=13.083511780742336, salary=6700, playerId=733672}, {position=WR, projection=11.223249904685241, salary=6600, playerId=749948}, {position=TE, projection=11.297493448672054, salary=6600, playerId=739424}, {position=RB, projection=10.755347257693705, salary=6600, playerId=823041}, {position=TE, projection=9.885905225180252, salary=6600, playerId=820699}, {position=QB, projection=16.282741879291194, salary=6600, playerId=867303}, {position=TE, projection=7.575941599937364, salary=6500, playerId=296480}, {position=QB, projection=10.592559739526903, salary=6500, playerId=946582}, {position=WR, projection=10.91630368761338, salary=6400, playerId=473742}, {position=WR, projection=8.397669596345231, salary=6400, playerId=461620}, {position=RB, projection=15.293568975044929, salary=6400, playerId=747861}, {position=RB, projection=9.86886730867097, salary=6400, playerId=746613}, {position=WR, projection=9.772577169132768, salary=6400, playerId=608360}, {position=TE, projection=10.18641675004519, salary=6400, playerId=744425}, {position=WR, projection=11.141258671309451, salary=6300, playerId=742387}, {position=RB, projection=6.138336067888705, salary=6300, playerId=865565}, {position=TE, projection=9.670854192173875, salary=6300, playerId=477386}, {position=WR, projection=8.307195197282258, salary=6200, playerId=865950}, {position=RB, projection=5.7147078727424825, salary=6200, playerId=606501}, {position=TE, projection=12.987549254365696, salary=6200, playerId=600191}, {position=WR, projection=10.846239223347238, salary=6200, playerId=822008}, {position=WR, projection=9.883871079071676, salary=6100, playerId=460830}, {position=WR, projection=10.507747599942967, salary=6100, playerId=658447}, {position=RB, projection=11.761783853518464, salary=6100, playerId=920062}, {position=RB, projection=11.114423844957951, salary=6100, playerId=694588}, {position=RB, projection=9.299096094063273, salary=6100, playerId=397945}, {position=RB, projection=11.158907364246472, salary=6000, playerId=543825}, {position=RB, projection=13.540261647186695, salary=6000, playerId=556294}, {position=RB, projection=13.764558202903286, salary=6000, playerId=592914}, {position=WR, projection=11.523534263901771, salary=6000, playerId=599649}, {position=WR, projection=11.671765458497717, salary=6000, playerId=835749}, {position=WR, projection=9.093665123826604, salary=5900, playerId=733754}, {position=RB, projection=11.173709343391371, salary=5900, playerId=835814}, {position=WR, projection=11.669401755941019, salary=5900, playerId=865801}, {position=RB, projection=1.530710169749752, salary=5900, playerId=693837}, {position=WR, projection=9.278741846698757, salary=5900, playerId=324216}, {position=RB, projection=9.254521802684469, salary=5800, playerId=880033}, {position=WR, projection=12.917870902882946, salary=5800, playerId=821389}, {position=TE, projection=5.004164873666191, salary=5800, playerId=494969}, {position=WR, projection=3.865035440957037, salary=5800, playerId=494313}, {position=RB, projection=6.936068928894251, salary=5700, playerId=744436}, {position=WR, projection=9.40605189118197, salary=5700, playerId=618715}, {position=RB, projection=4.1071343075257705, salary=5700, playerId=267443}, {position=WR, projection=9.231882756213924, salary=5700, playerId=246053}, {position=WR, projection=4.629155558163062, salary=5700, playerId=822014}, {position=RB, projection=6.144336060507369, salary=5600, playerId=498760}, {position=RB, projection=10.572856621478023, salary=5600, playerId=695311}, {position=RB, projection=4.860635991504624, salary=5600, playerId=750097}, {position=RB, projection=7.291983066042652, salary=5600, playerId=880146}, {position=TE, projection=6.438969012411015, salary=5600, playerId=465129}, {position=WR, projection=8.75112261202092, salary=5600, playerId=602118}, {position=WR, projection=9.050193320026128, salary=5600, playerId=836116}, {position=RB, projection=6.951719952864708, salary=5600, playerId=691583}, {position=WR, projection=5.370805608509121, salary=5600, playerId=837958}, {position=WR, projection=2.328131036213895, salary=5500, playerId=910431}, {position=TE, projection=4.766424451280045, salary=5500, playerId=295918}, {position=WR, projection=8.33417953944545, salary=5500, playerId=883459}, {position=WR, projection=6.199842855647865, salary=5500, playerId=652808}, {position=WR, projection=6.240649029095479, salary=5500, playerId=747906}, {position=RB, projection=5.16527985697486, salary=5500, playerId=696080}, {position=RB, projection=2.7763895370028546, salary=5500, playerId=749205}, {position=WR, projection=8.321990597071242, salary=5500, playerId=976220}, {position=RB, projection=9.944957138945854, salary=5500, playerId=832232}, {position=RB, projection=8.145348566351375, salary=5500, playerId=835694}, {position=RB, projection=6.347912573522058, salary=5400, playerId=552409}, {position=WR, projection=9.972317761701122, salary=5400, playerId=944826}, {position=WR, projection=4.415794697528436, salary=5400, playerId=922026}, {position=TE, projection=4.5213432062430465, salary=5400, playerId=732147}, {position=WR, projection=6.617425323682933, salary=5400, playerId=596417}, {position=WR, projection=6.737006523510496, salary=5400, playerId=263758}, {position=WR, projection=7.612475920095385, salary=5400, playerId=591801}, {position=TE, projection=0.5743002933222481, salary=5400, playerId=832316}, {position=TE, projection=8.259396438459436, salary=5400, playerId=836152}, {position=TE, projection=6.298726163538003, salary=5300, playerId=944428}, {position=RB, projection=3.628370617397926, salary=5300, playerId=823872}, {position=RB, projection=8.282207204352778, salary=5300, playerId=835779}, {position=RB, projection=4.2287049685487785, salary=5300, playerId=157341}, {position=RB, projection=5.885943096829511, salary=5300, playerId=467405}, {position=RB, projection=8.355311315246398, salary=5200, playerId=1108841}, {position=WR, projection=4.146265828250534, salary=5200, playerId=837503}, {position=TE, projection=4.6815169241492525, salary=5200, playerId=246082}, {position=WR, projection=1.2026744911066867, salary=5200, playerId=503184}, {position=WR, projection=6.5683736985736, salary=5200, playerId=691348}, {position=RB, projection=5.494675609665501, salary=5200, playerId=509372}, {position=WR, projection=5.910483799769109, salary=5100, playerId=880151}, {position=WR, projection=5.027254423513052, salary=5100, playerId=838179}, {position=WR, projection=4.85662449807971, salary=5100, playerId=692660}, {position=WR, projection=7.354741912856365, salary=5100, playerId=891115}, {position=WR, projection=7.586980937419801, salary=5100, playerId=540997}, {position=RB, projection=7.903627601371818, salary=5100, playerId=944343}, {position=WR, projection=3.6433797107847816, salary=5100, playerId=614302}, {position=TE, projection=3.6251014606091108, salary=5000, playerId=767297}, {position=WR, projection=9.114601593348949, salary=5000, playerId=556955}, {position=RB, projection=4.662375950643881, salary=5000, playerId=693429}, {position=WR, projection=5.991482254242652, salary=5000, playerId=867071}, {position=RB, projection=3.889535076579936, salary=5000, playerId=739799}, {position=WR, projection=6.677380132254492, salary=5000, playerId=335172}, {position=WR, projection=4.356311173889594, salary=5000, playerId=542896}, {position=D, projection=11.23, salary=5000, playerId=335}, {position=WR, projection=7.888953934150697, salary=5000, playerId=732121}, {position=RB, projection=3.5132920883510166, salary=5000, playerId=605335}, {position=TE, projection=6.117863742751905, salary=4900, playerId=743749}, {position=RB, projection=2.6690228482155507, salary=4900, playerId=866446}, {position=WR, projection=2.3387553510495365, salary=4900, playerId=749099}, {position=TE, projection=4.172191123181452, salary=4900, playerId=838396}, {position=WR, projection=3.132793767330995, salary=4900, playerId=246804}, {position=RB, projection=4.626369328337687, salary=4900, playerId=877784}, {position=WR, projection=4.9469242906122375, salary=4900, playerId=877596}, {position=WR, projection=3.7949487083171505, salary=4900, playerId=823040}, {position=WR, projection=3.7105398084098904, salary=4900, playerId=744595}, {position=RB, projection=9.917106308729291, salary=4900, playerId=403060}, {position=RB, projection=6.233941622975339, salary=4900, playerId=608353}, {position=WR, projection=2.4475394936272252, salary=4900, playerId=754412}, {position=TE, projection=3.2767579765543045, salary=4900, playerId=593305}, {position=TE, projection=4.201090717690165, salary=4900, playerId=693833}, {position=RB, projection=0.8542240995292695, salary=4900, playerId=459193}, {position=WR, projection=3.093019291869601, salary=4900, playerId=791008}, {position=WR, projection=1.9191453611220946, salary=4900, playerId=557415}, {position=TE, projection=3.007402938502456, salary=4900, playerId=832098}, {position=WR, projection=5.8307798461927645, salary=4800, playerId=920809}, {position=WR, projection=5.102878217087773, salary=4800, playerId=465652}, {position=WR, projection=1.7738327811889647, salary=4800, playerId=757521}, {position=D, projection=11.73, salary=4800, playerId=347}, {position=WR, projection=1.3562590517438402, salary=4800, playerId=507478}, {position=TE, projection=3.9283748645810426, salary=4800, playerId=608753}, {position=TE, projection=1.0514379206713615, salary=4800, playerId=744568}, {position=WR, projection=2.5001586831470934, salary=4800, playerId=742382}, {position=TE, projection=8.389935947348686, salary=4800, playerId=469472}, {position=RB, projection=4.743613819439407, salary=4800, playerId=865895}, {position=TE, projection=0.1778477509249914, salary=4800, playerId=752665}, {position=WR, projection=2.9496643508454916, salary=4800, playerId=750838}, {position=WR, projection=2.2035388769211552, salary=4800, playerId=867754}, {position=TE, projection=7.291182711738024, salary=4800, playerId=923911}, {position=WR, projection=6.043460309629914, salary=4800, playerId=836104}, {position=RB, projection=1.3880488558606676, salary=4800, playerId=590796}, {position=RB, projection=1.5949481080845516, salary=4800, playerId=832900}, {position=TE, projection=2.6159319915627677, salary=4700, playerId=865573}, {position=WR, projection=3.275280228479262, salary=4700, playerId=652520}, {position=RB, projection=1.1933516798404644, salary=4700, playerId=832473}, {position=WR, projection=1.9111163537812932, salary=4700, playerId=884553}, {position=WR, projection=4.161976210006264, salary=4700, playerId=748554}, {position=WR, projection=2.440822263196591, salary=4700, playerId=704254}, {position=RB, projection=4.403675802839744, salary=4700, playerId=703015}, {position=TE, projection=1.643209536053229, salary=4700, playerId=214197}, {position=RB, projection=4.644273390037035, salary=4700, playerId=494724}, {position=RB, projection=4.150771294863185, salary=4700, playerId=546076}, {position=TE, projection=2.907164011304044, salary=4700, playerId=507528}, {position=WR, projection=1.9971240075849226, salary=4700, playerId=558619}, {position=TE, projection=5.302615151991662, salary=4700, playerId=266414}, {position=WR, projection=1.0865724313148655, salary=4700, playerId=878911}, {position=WR, projection=6.382463575987019, salary=4700, playerId=746491}, {position=RB, projection=1.1801997263916502, salary=4600, playerId=691645}, {position=RB, projection=1.2845441986005954, salary=4600, playerId=501150}, {position=WR, projection=0.47090304163030294, salary=4600, playerId=822644}, {position=WR, projection=2.7926348626621564, salary=4600, playerId=830084}, {position=WR, projection=2.605273111964177, salary=4600, playerId=748755}, {position=WR, projection=2.2032117912572353, salary=4600, playerId=850603}, {position=TE, projection=2.656081375026433, salary=4600, playerId=400516}, {position=WR, projection=4.172245091624312, salary=4600, playerId=821159}, {position=D, projection=8.22, salary=4600, playerId=357}, {position=WR, projection=0.820356268293934, salary=4600, playerId=606551}, {position=D, projection=8.79, salary=4600, playerId=329}, {position=WR, projection=0.8435606634539368, salary=4600, playerId=562859}, {position=WR, projection=2.9389185306478227, salary=4600, playerId=604908}, {position=D, projection=8.31, salary=4600, playerId=325}, {position=RB, projection=8.974366116910454, salary=4600, playerId=598969}, {position=RB, projection=0.3956721629492877, salary=4600, playerId=821729}, {position=TE, projection=3.759571319650812, salary=4600, playerId=552926}, {position=WR, projection=4.051741488449973, salary=4600, playerId=591586}, {position=RB, projection=0.4212258288695862, salary=4500, playerId=842181}, {position=RB, projection=0.3416085843178473, salary=4500, playerId=839043}, {position=WR, projection=4.1313574577409335, salary=4500, playerId=838415}, {position=WR, projection=0.09894722723005703, salary=4500, playerId=865803}, {position=WR, projection=0.2371029298407441, salary=4500, playerId=910852}, {position=RB, projection=0.1832803032798287, salary=4500, playerId=910605}, {position=RB, projection=2.4165889435946615, salary=4500, playerId=915397}, {position=WR, projection=0.7397824596660577, salary=4500, playerId=913826}, {position=TE, projection=0.42927785049081757, salary=4500, playerId=881697}, {position=WR, projection=0.8566641630550769, salary=4500, playerId=1166036}, {position=WR, projection=0.3983162952535659, salary=4500, playerId=884567}, {position=RB, projection=1.6055371462607257, salary=4500, playerId=923109}, {position=WR, projection=0.3485553037863783, salary=4500, playerId=832220}, {position=RB, projection=0.15524193617426904, salary=4500, playerId=887450}, {position=RB, projection=0.17179254310160957, salary=4500, playerId=700813}, {position=WR, projection=0.329947019974951, salary=4500, playerId=787757}, {position=RB, projection=0.5317345651024142, salary=4500, playerId=1049905}, {position=RB, projection=2.5061092986310425, salary=4500, playerId=884791}, {position=RB, projection=3.5473271643969553, salary=4500, playerId=884549}, {position=WR, projection=0.45079663452336205, salary=4500, playerId=1166990}, {position=WR, projection=1.1303517055235641, salary=4500, playerId=866112}, {position=RB, projection=0.5932225714824363, salary=4500, playerId=866956}, {position=WR, projection=0.58011359808964, salary=4500, playerId=612512}, {position=RB, projection=0.7160385453606106, salary=4500, playerId=568874}, {position=WR, projection=0.20613495779797072, salary=4500, playerId=883976}, {position=WR, projection=0.32400013950227474, salary=4500, playerId=883432}, {position=WR, projection=2.8919733266910224, salary=4500, playerId=836936}, {position=TE, projection=3.812997273938738, salary=4500, playerId=884083}, {position=WR, projection=0.17821972335858735, salary=4500, playerId=1166545}, {position=RB, projection=0.5866910160557077, salary=4500, playerId=880322}, {position=TE, projection=3.413350403417888, salary=4500, playerId=881956}, {position=RB, projection=0.3283070546753704, salary=4500, playerId=866115}, {position=RB, projection=0.32639236755195605, salary=4500, playerId=553705}, {position=TE, projection=3.465939588036292, salary=4500, playerId=508968}, {position=WR, projection=0.743255697642362, salary=4500, playerId=686800}, {position=RB, projection=0.14372013838469522, salary=4500, playerId=694015}, {position=WR, projection=0.5964531446696092, salary=4500, playerId=557177}, {position=RB, projection=0.4372167905994134, salary=4500, playerId=714230}, {position=WR, projection=1.1022512919462715, salary=4500, playerId=744883}, {position=WR, projection=1.8475646516988085, salary=4500, playerId=746191}, {position=RB, projection=0.3457201580995941, salary=4500, playerId=733248}, {position=WR, projection=4.534936973093353, salary=4500, playerId=727279}, {position=TE, projection=3.007929854315065, salary=4500, playerId=469509}, {position=WR, projection=0.2093432111779464, salary=4500, playerId=607847}, {position=TE, projection=1.0347277070573389, salary=4500, playerId=602269}, {position=RB, projection=0.42377271963883123, salary=4500, playerId=400947}, {position=WR, projection=0.9127037277849662, salary=4500, playerId=329031}, {position=RB, projection=1.6915013620518629, salary=4500, playerId=465752}, {position=TE, projection=4.293090808985734, salary=4500, playerId=605423}, {position=RB, projection=1.230984416452476, salary=4500, playerId=691856}, {position=RB, projection=0.9431325832646666, salary=4500, playerId=606530}, {position=TE, projection=1.4064319525163602, salary=4500, playerId=592268}, {position=RB, projection=0.3446315343470543, salary=4500, playerId=556483}, {position=WR, projection=0.9141667040826671, salary=4500, playerId=696881}, {position=RB, projection=0.09140878120650894, salary=4500, playerId=835845}, {position=WR, projection=0.2887431421515672, salary=4500, playerId=835861}, {position=WR, projection=0.7104452968671064, salary=4500, playerId=824541}, {position=RB, projection=0.31144037032618543, salary=4500, playerId=833687}, {position=RB, projection=1.222418675931948, salary=4500, playerId=830860}, {position=WR, projection=0.17234858058527516, salary=4500, playerId=835417}, {position=WR, projection=0.9943704068808661, salary=4500, playerId=820866}, {position=RB, projection=0.0338927095173335, salary=4500, playerId=602461}, {position=RB, projection=0.7611781477951224, salary=4500, playerId=558973}, {position=RB, projection=0.31227273967906516, salary=4500, playerId=835830}, {position=WR, projection=2.4120012153083876, salary=4500, playerId=741322}, {position=TE, projection=1.750733856289133, salary=4400, playerId=651658}, {position=D, projection=4.89, salary=4400, playerId=336}, {position=TE, projection=0.07925858786529812, salary=4400, playerId=332066}, {position=TE, projection=1.2036224828719821, salary=4300, playerId=552586}, {position=D, projection=6.55, salary=4300, playerId=365}, {position=D, projection=10.4, salary=4300, playerId=356}, {position=TE, projection=0.6019602200618658, salary=4300, playerId=561380}, {position=D, projection=7.01, salary=4200, playerId=366}, {position=D, projection=5.75, salary=4200, playerId=348}, {position=TE, projection=2.3158916905480043, salary=4200, playerId=606055}, {position=TE, projection=2.8368541478566893, salary=4200, playerId=593517}, {position=TE, projection=0.9154375129216434, salary=4200, playerId=824428}, {position=D, projection=9.4, salary=4100, playerId=352}, {position=TE, projection=0.5853383066262842, salary=4000, playerId=334184}, {position=TE, projection=0.03713041353566638, salary=4000, playerId=403184}, {position=TE, projection=0.21163039108113463, salary=4000, playerId=334186}, {position=TE, projection=1.567272699395569, salary=4000, playerId=712506}, {position=TE, projection=1.2813259441496738, salary=4000, playerId=654887}, {position=D, projection=8.2, salary=4000, playerId=338}, {position=TE, projection=0.9984828007309315, salary=4000, playerId=1115394}, {position=TE, projection=1.1665316789461373, salary=4000, playerId=660151}, {position=TE, projection=0.12351008366597117, salary=4000, playerId=323254}, {position=TE, projection=2.4521054534298057, salary=4000, playerId=563293}, {position=TE, projection=0.6577526670265371, salary=4000, playerId=542871}, {position=TE, projection=0.8535552485202904, salary=4000, playerId=610943}, {position=TE, projection=1.411421204779341, salary=4000, playerId=884045}, {position=TE, projection=0.4736576353540855, salary=4000, playerId=821736}, {position=TE, projection=0.08130877850588217, salary=4000, playerId=694088}, {position=TE, projection=1.5272307604061448, salary=4000, playerId=749968}, {position=TE, projection=1.312897904251889, salary=4000, playerId=742474}, {position=TE, projection=0.2487208149436048, salary=4000, playerId=747895}, {position=TE, projection=0.12196316775882327, salary=4000, playerId=728274}, {position=TE, projection=1.332848412559489, salary=4000, playerId=861278}, {position=TE, projection=0.08211348956971853, salary=4000, playerId=877598}, {position=TE, projection=7.454902456276206, salary=4000, playerId=943093}, {position=TE, projection=0.3337785152268145, salary=4000, playerId=919456}, {position=TE, projection=3.095384936041896, salary=4000, playerId=652019}, {position=TE, projection=0.37347643337478975, salary=4000, playerId=914008}, {position=TE, projection=1.745831679748529, salary=4000, playerId=606488}, {position=D, projection=7.57, salary=3800, playerId=350}, {position=D, projection=5.79, salary=3800, playerId=362}, {position=D, projection=4.81, salary=3800, playerId=359}, {position=D, projection=5.54, salary=3800, playerId=332}, {position=D, projection=6.31, salary=3700, playerId=345}, {position=D, projection=10.48, salary=3500, playerId=323}, {position=D, projection=7.39, salary=3500, playerId=364}, {position=D, projection=6.54, salary=3500, playerId=327}, {position=D, projection=6.35, salary=3500, playerId=355}, {position=D, projection=4.92, salary=3400, playerId=341}, {position=D, projection=5.23, salary=3400, playerId=324}, {position=D, projection=4.28, salary=3300, playerId=334}, {position=D, projection=5.83, salary=3200, playerId=339}, {position=D, projection=4.05, salary=3000, playerId=363}]";
        List<String> playerStringList = Arrays.asList(playerListString.replace("[", "")
                .replace("]", "").split("}, "));
        playerStringList.forEach(playerString -> playerList.add(new Player(playerString)));
        List<Player> whiteList = new ArrayList<>();
        List<Player> blackList = new ArrayList<>();
        int salaryCap = 60000;
        optimizer = new Optimizer(playerList, whiteList, blackList, lineupMatrix, salaryCap);
        List<Player> testBlackList = Collections.singletonList(new Player(868199));
        optimizerWithWhiteList = new Optimizer(playerList, testWhiteList, testBlackList, lineupMatrix, salaryCap);
    }

    @Test
    void shouldReturnOptimalLineup() {
        Set<Integer> result = optimizer.getOptimalLineup().stream().map(player -> player.playerId).collect(Collectors.toSet());
        Set<Integer> expected = Stream.of(
                new Player(828743),
                new Player(747861),
                new Player(750846),
                new Player(877790),
                new Player(653699),
                new Player(884013),
                new Player(600191),
                new Player(592914),
                new Player(323)
        ).map(player -> player.playerId).collect(Collectors.toSet());
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnPositionThreshold() {
        int result1 = optimizer.positionThreshold("RB,WR,TE");
        assertEquals(10, result1);
        int result2 = optimizer.positionThreshold("WR");
        assertEquals(9, result2);
        int result3 = optimizer.positionThreshold("QB");
        assertEquals(5, result3);
    }

    @Test
    void shouldReturnTruncatedPools() {
        List<List<Player>> result = optimizer.getTruncatedPlayerPoolsByPosition();
        assertEquals(6, result.size());
        assertEquals(5, result.get(0).size());
        assertEquals(8, result.get(1).size());
        assertEquals(9, result.get(2).size());
        assertEquals(5, result.get(3).size());
        assertEquals(10, result.get(4).size());
        assertEquals(5, result.get(5).size());
    }

    @Test
    void shouldReturnEachSetOfCombinations() {
        List<Set<List<Player>>> result = optimizer.permutePlayerPools(optimizer.getTruncatedPlayerPoolsByPosition());
        assertEquals(6, result.size());
        assertEquals(5, result.get(0).size());
        assertEquals(28, result.get(1).size());
        assertEquals(84, result.get(2).size());
        assertEquals(5, result.get(3).size());
        assertEquals(10, result.get(4).size());
        assertEquals(5, result.get(5).size());
    }

    @Test
    void shouldGetCorrectCartesianProductSize() {
        List<Set<List<Player>>> playerPools = optimizer.permutePlayerPools(optimizer.getTruncatedPlayerPoolsByPosition());
        Set<List<List<Player>>> result = Sets.cartesianProduct(playerPools);
        assertEquals(5*28*84*5*10*5, result.size());
    }

    @Test
    void shouldHandleOptimizerWhiteAndBlackListInput() {
        Set<Integer> result = optimizerWithWhiteList.getOptimalLineup().stream().map(player -> player.playerId).collect(Collectors.toSet());
        Set<Integer> expected = Stream.of(
                new Player(828743),
                new Player(456613),
                new Player(592914),
                new Player(877790),
                new Player(821389),
                new Player(653699),
                new Player(600191),
                new Player(750846),
                new Player(323)
        ).map(player -> player.playerId).collect(Collectors.toSet());
        assertEquals(expected, result);
        assertTrue(result.contains(456613));
        assertTrue(!result.contains(868199));
        assertTrue(!result.contains(0));
    }

    @Test
    void shouldReturnTruncatedPoolsWithWhiteList() {
        List<List<Player>> result = optimizerWithWhiteList.getTruncatedPlayerPoolsByPosition();
        assertEquals(6, result.size());
        assertEquals(5, result.get(0).size());
        assertEquals(5, result.get(1).size());
        assertEquals(9, result.get(2).size());
        assertEquals(5, result.get(3).size());
        assertEquals(10, result.get(4).size());
        assertEquals(5, result.get(5).size());
    }

    @Test
    void shouldReturnEachSetOfCombinationsWithWhiteList() {
        List<Set<List<Player>>> result = optimizerWithWhiteList.permutePlayerPools(optimizerWithWhiteList.getTruncatedPlayerPoolsByPosition());
        assertEquals(6, result.size());
        assertEquals(5, result.get(0).size());
        assertEquals(5, result.get(1).size());
        assertEquals(84, result.get(2).size());
        assertEquals(5, result.get(3).size());
        assertEquals(10, result.get(4).size());
        assertEquals(5, result.get(5).size());
    }

    @Test
    void shouldGetCorrectCartesianProductSizeWithWhiteList() {
        List<Set<List<Player>>> playerPools = optimizerWithWhiteList.permutePlayerPools(optimizerWithWhiteList.getTruncatedPlayerPoolsByPosition());
        Set<List<List<Player>>> result = Sets.cartesianProduct(playerPools);
        assertEquals(5*5*84*5*10*5, result.size());
    }

    @Test
    void shouldGetLineupWithWhiteListedPlayers() {
        Player emptyPlayer = new Player();
        List<Player> result = optimizerWithWhiteList.getLineupWithWhiteList(testWhiteList, lineupMatrix);
        List<Player> expected = Arrays.asList(
                emptyPlayer, new Player(456613), emptyPlayer, emptyPlayer, emptyPlayer, emptyPlayer,
                emptyPlayer, emptyPlayer, emptyPlayer
        );
        assertEquals(expected, result);
    }

    @Test
    void shouldRemoveWhiteListedPositionsFromLineupMatrix() {
        List<String> result = optimizerWithWhiteList.lineupMatrixWithoutWhiteListedPositions(optimizerWithWhiteList.getLineupWithWhiteList(testWhiteList, lineupMatrix), lineupMatrix);
        assertEquals(Arrays.asList("QB", "RB", "WR", "WR", "WR", "TE", "RB,WR,TE", "D"), result);
    }

    @Test
    void shouldCheckForValidLineup() {
        boolean result1 = optimizer.isValidLineup(Arrays.asList(
                new Player(828743),
                new Player(747861),
                new Player(750846),
                new Player(877790),
                new Player(653699),
                new Player(884013),
                new Player(600191),
                new Player(592914),
                new Player(323)
        ));
        assertTrue(result1);
        boolean result2 = optimizer.isValidLineup(Arrays.asList(
                new Player(828743),
                new Player(747861),
                new Player(750846),
                new Player(877790)
        ));
        assertTrue(result2);
        boolean result3 = optimizer.isValidLineup(Arrays.asList(
                new Player(828743),
                new Player(747861),
                new Player(750846),
                new Player(877790),
                new Player(653699),
                new Player(884013),
                new Player(600191),
                new Player(747861),
                new Player(323)
        ));
        assertFalse(result3);
    }

    @Test
    void shouldCombineOptimalPlayersWithWhiteList() {
        List<Player> lineupWithWhiteList = Arrays.asList(
                new Player(),
                new Player(456613),
                new Player(),
                new Player(),
                new Player(),
                new Player(),
                new Player(),
                new Player(),
                new Player()
        );
        List<Player> optimalPlayers = Arrays.asList(
                new Player(828743),

                new Player(750846),
                new Player(877790),
                new Player(653699),
                new Player(884013),
                new Player(600191),
                new Player(592914),
                new Player(323)
        );
        List<Player> result = optimizerWithWhiteList.combineOptimalPlayersWithWhiteList(optimalPlayers, lineupWithWhiteList);
        assertEquals(Arrays.asList(
                new Player(828743),
                new Player(456613),
                new Player(750846),
                new Player(877790),
                new Player(653699),
                new Player(884013),
                new Player(600191),
                new Player(592914),
                new Player(323)
        ), result);
    }
}
