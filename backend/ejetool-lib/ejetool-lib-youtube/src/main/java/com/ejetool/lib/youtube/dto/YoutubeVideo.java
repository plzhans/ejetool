package com.ejetool.lib.youtube.dto;

import lombok.Data;

@Data
public class YoutubeVideo {
    
    private String id;

    private YoutubeVideoStatus status;

    private YoutubeVideoSnippet snippet;

    public String getUrl(){
        return "https://www.youtube.com/watch?v=" + this.id;
    }
}

// {
//     "id": "-i1F63e_tPg",
//     "status": {
//         "uploadStatus": "uploaded",
//         "privacyStatus": "public",
//         "license": "youtube",
//         "embeddable": false,
//         "publicStatsViewable": false,
//         "selfDeclaredMadeForKids": false
//     },
//     "snippet": {
//         "channelId": "UC9wnpGyTlZdp3WSEgtySBIw",
//         "title": "도전에 관한 명언 7가지 #도전 #명언",
//         "description": "도전에 관한 명언 7가지\n\n자신의 한계를 넘어선다는 것은 곧 새로운 시작이다. 우리는 끊임없이 도전하고 성장해야 한다. 우리 모두가 특별하다.\n실패는 새로운 기회다. 실패를 두려워하지 말고 그 속에서 더욱 강해지고 배우자.\n우리의 삶은 우리의 선택에 따라 달라진다. 긍정적인 마음가짐으로 선택하자.\n계획 없는 목표는 그냥 꿈이다. 우리는 계획을 세우고 행동으로 이뤄져야 한다.\n어제는 과거, 내일은 미래. 지금 이 순간을 소중히 여기고 행동하자.\n어떤 시련이 와도 포기하지 말고 끊임없이 노력하자. 매 순간이 성공을 향한 발걸음이다.\n최선을 다해도 실패할 수 있지만, 끊임없이 노력하는 사람은 결국 성공한다.\n\nPushing beyond our limits is the start of something new. We must constantly challenge ourselves and grow. We are all special.\nFailure is a new opportunity. Do not fear failure, but rather grow stronger and learn from it.\nOur lives are shaped by our choices. Let us choose with a positive mindset.\nA goal without a plan is just a wish. We must make plans and take action to achieve it.\nYesterday is history, tomorrow is a mystery. Cherish this moment and take action now.\nNo matter what hardships come, do not give up and keep striving. Every moment is a step towards success.\nEven with your best effort, you may fail, but those who keep striving will eventually succeed.\n\n#shorts #과거 #목표 #강함 #선택 #기회 #행동 #성장 #순간 #특별 #시련 #도전 #삶 #미래 #노력 #실패 #계획 #성공 #실패 #최선 #긍정\n\ncreated by ejegong AI",
//         "thumbnails": {
//             "default": {
//                 "url": "https://i.ytimg.com/vi/-i1F63e_tPg/default.jpg",
//                 "width": 120,
//                 "height": 90
//             },
//             "medium": {
//                 "url": "https://i.ytimg.com/vi/-i1F63e_tPg/mqdefault.jpg",
//                 "width": 320,
//                 "height": 180
//             },
//             "high": {
//                 "url": "https://i.ytimg.com/vi/-i1F63e_tPg/hqdefault.jpg",
//                 "width": 480,
//                 "height": 360
//             }
//         },
//         "channelTitle": "eje의 인용구 세상",
//         "categoryId": "27",
//         "liveBroadcastContent": "none",
//         "localized": {
//             "title": "도전에 관한 명언 7가지 #도전 #명언",
//             "description": "도전에 관한 명언 7가지\n\n자신의 한계를 넘어선다는 것은 곧 새로운 시작이다. 우리는 끊임없이 도전하고 성장해야 한다. 우리 모두가 특별하다.\n실패는 새로운 기회다. 실패를 두려워하지 말고 그 속에서 더욱 강해지고 배우자.\n우리의 삶은 우리의 선택에 따라 달라진다. 긍정적인 마음가짐으로 선택하자.\n계획 없는 목표는 그냥 꿈이다. 우리는 계획을 세우고 행동으로 이뤄져야 한다.\n어제는 과거, 내일은 미래. 지금 이 순간을 소중히 여기고 행동하자.\n어떤 시련이 와도 포기하지 말고 끊임없이 노력하자. 매 순간이 성공을 향한 발걸음이다.\n최선을 다해도 실패할 수 있지만, 끊임없이 노력하는 사람은 결국 성공한다.\n\nPushing beyond our limits is the start of something new. We must constantly challenge ourselves and grow. We are all special.\nFailure is a new opportunity. Do not fear failure, but rather grow stronger and learn from it.\nOur lives are shaped by our choices. Let us choose with a positive mindset.\nA goal without a plan is just a wish. We must make plans and take action to achieve it.\nYesterday is history, tomorrow is a mystery. Cherish this moment and take action now.\nNo matter what hardships come, do not give up and keep striving. Every moment is a step towards success.\nEven with your best effort, you may fail, but those who keep striving will eventually succeed.\n\n#shorts #과거 #목표 #강함 #선택 #기회 #행동 #성장 #순간 #특별 #시련 #도전 #삶 #미래 #노력 #실패 #계획 #성공 #실패 #최선 #긍정\n\ncreated by ejegong AI"
//         },
//         "defaultAudioLanguage": "en-US",
//         "publishedAt": "2024-07-29T07:42:40.000Z"
//     },
//     "embedHtml": "<iframe width=\"480\" height=\"360\" src=\"//www.youtube.com/embed/-i1F63e_tPg\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>",
//     "contentDetails": {
//         "duration": "P0D",
//         "dimension": "2d",
//         "definition": "sd",
//         "caption": "false",
//         "licensedContent": false,
//         "contentRating": {},
//         "projection": "rectangular",
//         "hasCustomThumbnail": false
//     }
// }
