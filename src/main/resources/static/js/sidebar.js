console.log("JavaScript fiel has activated")

const toggleSidebar=()=>
{
    if($(".sidebar").is(":visible"))
    {   //if-true close the side bar
        $(".sidebar").css("display", "none")
        $(".content").css("margin-left","0%")
    }else{

        $(".sidebar").css("display", "block")
        $(".content").css("margin-left","20%")
    }
};