package com.ejetool.lib.creatomate.dto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RenderStatus {
    
    /** the render is queued for rendering */
    public static final String PLANNED = "planned";
    
    /** the render is waiting for a third-party service (e.g., OpenAI or ElevenLabs) to finish */
    public static final String WAITING = "waiting";

    /** an input file is being transcribed */
    public static final String TRANSCRIBING = "transcribing"; 

    /** the render is being processed */
    public static final String RENDERING = "rendering";

    /** the render has been completed successfully */
    public static final String SUCCEEDED = "succeeded"; 

    /** the render failed due to the reason specified in the error_message field */
    public static final String FAILED = "failed"; 
}
