package com.busanit501.springproject3.hjt.service;

import com.busanit501.springproject3.hjt.domain.HjtEntity;
import com.busanit501.springproject3.hjt.repository.HjtRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class HjtService {

    @Autowired
    private HjtRepository hjtRepository;

    public List<HjtEntity> findAll() {
        return hjtRepository.findAll();
    }

    public Optional<HjtEntity> findById(Long id) {
        return hjtRepository.findById(id);
    }

    public void saveTools() {
        HjtEntity hammer = HjtEntity.builder()
                .tool_name("망치")
                .description("뭔가를 깨거나 못 등을 박는 용도로 주로 쓰이며, 공사현장에서는 작업의 종류에 따라 길이, 무게, 재질(고무, 구리, 쇠 등)이 다른 여러 종류의 망치를 사용한다.\n" +
                        "보통 가정에서는 뒤에 노루발(못을 뽑을 때 사용하는 갈고리)이 달려 못을 박을 수도 있고 뽑을 수도 있는 장도리를 쓴다. 정확히는 슬레지해머처럼 큰 게 '망치'였고 작은 건 '마치'라고 했지만, 1980년대에도 이미 교과서에서 밖에 쓰지 않는 말이었고, 현재는 아예 쓰이지 않는 사어가 되었다. 한국에서는 슬렛지해머를 '오함마'라고 부르기도 한다.\n" +
                        "말 그대로 쇠뭉치에 손잡이만 달아놓은 모양새라서 단순해 보이지만, 의외로 현대 철강 기술력의 집약체이기도 하다. 용도상 엄청난 충격을 지속적으로 견뎌내야 함과 동시에, 너무 무거워도 안 되고 너무 비싸도 안된다는 제약까지 지키면서 만들어야 하기 때문에 어지간한 기술력으로는 명함 조차 못 내민다. 잘 만든 망치머리의 강도는 상상을 초월할 정도로 단단하다.\n ")
                .img_text("hammer.jpg")
                .build();

        HjtEntity nipper = HjtEntity.builder()
                .tool_name("니퍼")
                .description("피복된 전선을 드러내기 위해 절연 전선의 피복을 벗기거나, 가는 전선이나 철사 등 선재를 절단할 때 쓰는 공구다.\n" +
                        "니퍼의 종류는 굉장히 다양한데 우리가 흔히 알고 있는 니퍼는 대각선형 컷팅식 니퍼(Diagonal Cutting Nipper)다. 워낙에 이 형태의 니퍼가 대중화되다 보니 니퍼 하면 십중팔구 대각선 날 모양 니퍼를 칭한다. 영미권에서는 니퍼라는 명칭보다 주로 \"사이드 커터\"(Side Cutter)나 \"대각선 절단 플라이어(Diagonal Cutting Plier)\"라고 부른다.\n" +
                        "지레의 원리를 사용했는데, 악력이 배로 늘어나 날에 가해지도록 구조가 되어 있다. 니퍼의 종류는 보통니퍼(빗날니퍼)와 강력니퍼로 나뉜다. 주로 철선, 강선, 구리선 등을 절단하거나 전선 등의 피복을 벗기는 데 사용된다. 비슷한 용도로 와이어 스트리퍼가 있다.")
                .img_text("nipper.jpg")
                .build();

        HjtEntity ruler = HjtEntity.builder()
                .tool_name("줄자")
                .description("줄로 된 자. 보통 플라스틱 막대 자로 잴 수 있는 길이보다 긴 길이를 잴 때 쓰인다.\n" +
                        "단순히 치수를 재는 역할이지만, 사용 환경은 제각기 다르기 때문에 일상생활에서 쓰는 것은 길이 3~7m 정도에 폭은 약 17~20mm 정도가 되어 작고 휴대하기 편하거나 스마트폰에 걸고 다닐 수 있는 것으로 생산되는 것도 많지만, 산업현장에서 쓰이는 것은 무겁고 긴 것도 많으며, 개중에서는 50m 이상의 거리를 재기 위해 사용하는 것으로 그 넓이가 사람 얼굴만큼 크거나 그보다 큰 것도 있다.\n" +
                        "길이에 따라 1.5m는 수예용이거나 허리둘레 재는 용도, 2~3m정도까지는 열쇠고리 사이즈로 제작되는 것도 있고, 3.5~5m쯤 가면 스톱퍼가 달려 있다.\n" +
                        "보통 긴 줄자의 양면에 눈금이 표시되어 있다. 간혹 서로 다른 단위 2가지를 동시에 표시해두기도 했다. 특히 센티미터/인치 눈금은 허리둘레 재는 용도의 줄자에 흔히 볼 수 있다.")
                .img_text("tape_measure.jpg")
                .build();

        HjtEntity grinder = HjtEntity.builder()
                .tool_name("그라인더")
                .description("고속회전하는 원반형태의 날이나 원형컵으로 표면을 매끄럽게 갈아내는 전동공구. 절단기와 비슷하다. 전기를 동력으로 사용하는것이 가장 흔하다.\n" +
                        "그라인더라고 하면 모든 연마기기를 지칭하는 말이지만 공구상이든 일반인이든 보통은 한 손으로 잡고 작업할 수 있을 정도로 작은 전동식 소형 앵글 그라인더로 알아 듣는다. 벤치 그라인더(탁상 그라인더)라고 모터에 연마석 등을 달아 사용하는 물건도 있고, 그 외에도 벨트 그라인더 에어그라인더 등 몇 가지 종류가 있다.\n" +
                        "그라인더는 회전하는 날로 연마하는 거고, 진동으로 연마하는 기구는 샌더라고 부른다.")
                .img_text("grinder.jpg")
                .build();

        HjtEntity driver = HjtEntity.builder()
                .tool_name("드라이버")
                .description("일상 생활에서 나사를 돌려 끼우고 빼는 도구. 정확한 이름은 스크루드라이버(Screwdriver)지만, 콩글리시로는 보통 드라이버로 불린다.\n" +
                        "가장 기본적이자 대중적인 스크루드라이버로는 영미권에서는 필립스(Phillips) 드라이버라고도 불리는 십(十)자 스크루 드라이버와, 그보다 훨씬 오래된 역사를 가진 일(一)자(Slotted) 스크루 드라이버가 있다. 처음에는 일자 스크루드라이버만 있었지만, 나중에 보다 편리한 십자 스크루드라이버가 개발되었다.\n" +
                        "하지만 여전히 큰 문제가 있었는데, 드라이버에 지나치게 강한 힘이 가해지면 드라이버 날이 볼트 홈의 빗면을 타고 밀려올라가면서 체결이 풀려 헛돌게 되고, 이로 인해 볼트와 드라이버 날 끝부분이 갈려나가게 된다는 것. 사실 개발 당시(1936년)에 이는 어느정도 의도된 것이었다. 강한 힘이 가해질 때 볼트와 드라이버의 체결이 유지되면 그 힘을 버티다못해 볼트나 드라이버가 통째로 부러질 위험성이 있었고, 이는 볼트가 갈려나가는 것보다 손해가 클 수 있었기 때문이다.")
                .img_text("screwdriver.jpg")
                .build();

        HjtEntity drill = HjtEntity.builder()
                .tool_name("전동 드릴")
                .description("전동공구의 얼굴마담으로 전기의 힘을 이용해 드릴비트나 스크류비트를 돌려 구멍을 뚫거나 나사를 고정시키는 공구이다.\n" +
                        "앞에 툴을 뭘 다느냐에 따라 전동 드라이버도 되고 전동 태퍼도 된다. 드릴은 말할 것도 없다.\n" +
                        "드릴 앞부분에 끼우는 날 같은 것을 비트, 또는 기리라고하며 그것을 드릴에 고정시키는 입 같은 부분을 척이라고 한다. 척은 특별한 도구없이 돌리면 고정이되는 키레스척과 별도의 키를 사용해 고정시키는 키척이 있는데, 키레스척은 간편하다는 장점이 있지만 연속적인 작업이나 강한 힘을 사용했을 때 풀려버리는 단점이 있으며, 반대로 키척은 키가 없으면 비트 교체가 힘들다는 단점이 있지만 단단히 고정해두면 웬만해선 빠지지 않는 장점이 있다.")
                .img_text("drill.jpg")
                .build();

        HjtEntity spanner = HjtEntity.builder()
                .tool_name("스패너")
                .description("스크루드라이버와 비슷하게 너트나 볼트 따위를 죄고 풀며 물체를 조립하고 분해할 때 사용하는 도구. 단어 뜻자체는 '비틀다'는 뜻이다. 스패너나 렌치나 동일한 것으로, 영국, 호주, 뉴질랜드 같은 나라들에서는 스패너라고 부르고 미국식 영어로는 렌치라고 부른다.\n" +
                        "멍키렌치(멍키스패너 또는 간단히 멍키라고도 한다)도 스패너의 일종이다.\n" +
                        "이것은 입의 크기를 어느 범위 내에서 자유로이 조정할 수 있으며, 개구부의 한쪽이 나사를 돌림으로써 움직이게 되어 있다. 1개의 멍키렌치로 치수의 크기가 다른 나사를 돌리는 데 이용할 수 있으나 강도가 약하다.\n" +
                        "각종 스패너나 멍키렌치의 재료로서는 단조강 또는 가단주철이 쓰인다. 기계를 설계할 때는 모두 너트를 돌릴 때의 스패너 운동에 필요한 여유를 예상해야 한다. ")
                .img_text("spanner.jpg")
                .build();

        HjtEntity scissors = HjtEntity.builder()
                .tool_name("공업용 가위")
                .description("가위는 두 개의 날을 교차시켜 물체를 자를 수 있도록 만들어진 도구로 실생활에서 널리 쓰인다.\n" +
                        "겉모양만 봐서는 잘 연상되지 않지만 지렛대의 종류 중 하나로 1종 지레에 속한다.\n" +
                        "동양과 서양 모두 고대부터 존재했던 것으로 파악된다. 칼 두 개를 X자로 교차시킨 다음에 교차점을 고정시키는 간단한 방법으로 만들 수 있었기 때문이다. 서양에서는 헬레니즘 시대부터 존재했고, 중국은 전한 시대의 것이 가장 오래된 것으로 알려져 있다. 그리고 중국에서 한국을 거쳐 일본에도 전해졌다.")
                .img_text("scissors.jpg")
                .build();

        HjtEntity saw = HjtEntity.builder()
                .tool_name("톱")
                .description("좁고 긴 쇠판에 날을 일정한 간격으로 이[齒]와 같이 내어 톱틀에 끼워서 둘 또는 혼자, 앞뒤로 문질러 나무나 돌을 자르는 데 사용하는 연장.\n" +
                        "금속판이나 쇠줄 표면 등에 삐죽삐죽한 톱니를 만들어 물건을 자르거나 켜는데 사용하는 공구의 총칭. 우리말로 나뭇결의 수직 방향으로 베는 것을 '자르다'라고 하고, 나뭇결 방향으로 베는 것은 '켜다'라고 한다.\n" +
                        "몸통의 머리 부분을 삼각형으로 하였으며 끝에는 손잡이가 달려 있다. 손잡이 부분에 목질(木質)의 흔적이 있고, 날은 약간 위로 굽어져 있다.\n" +
                        "상하 양쪽에 톱날이 있는데 아래쪽 날이 위쪽보다 크고 날은 좌우로 어긋나 있으며, 톱날들은 몸체의 앞을 향하여 날이 서 있는 것으로 미루어 앞으로 밀어서 나무를 절단하는 톱으로 보인다.")
                .img_text("saw.jpg")
                .build();

        HjtEntity calipers = HjtEntity.builder()
                .tool_name("버니어 캘리퍼스")
                .description("길이나 높이, 너비 등 기계류의 혹은 사람의 신체 부위 치수를 정밀하게 측정하는 자의 일종이다. 공학도와 인류학자(골학 측정용)의 필수품이다.\n" +
                        "구조는 주척(어미자)과 부척(아들자)으로 나뉘며 말그대로 가운데에 있는 것이 주척이고 앞뒤로 왔다갔다 움직이는 것이 부척이다.\n" +
                        "계산자와 비슷하게, 고정된 어미자와 움직이는 아들자로 구성되어 아들자를 움직여 움직인 길이를 측정한다. 일반적인 길이 측정뿐 아니라 철판의 두께, 또는 틈 사이의 간격이나 파이프의 직경이나 내경, 파인 구멍의 깊이 따위도 측정할 수 있다. 재질은 스테인리스 재질이 대부분이고 길이는 주로 최대 측정 가능 치수가 150, 200, 300 mm 짜리이다.")
                .img_text("vernier_calipers.jpg")
                .build();

        hjtRepository.save(hammer);
        hjtRepository.save(nipper);
        hjtRepository.save(ruler);
        hjtRepository.save(grinder);
        hjtRepository.save(driver);
        hjtRepository.save(drill);
        hjtRepository.save(spanner);
        hjtRepository.save(scissors);
        hjtRepository.save(saw);
        hjtRepository.save(calipers);
    }

    public Optional<HjtEntity> findByToolName(String toolName) {
        return hjtRepository.findAll().stream()
                .filter(tool -> tool.getTool_name().replace(" ","").equalsIgnoreCase(toolName.trim()))
                .findFirst();
    }

}


