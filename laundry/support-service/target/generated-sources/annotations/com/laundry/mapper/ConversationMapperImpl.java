package com.laundry.mapper;

import com.laundry.dto.request.ConversationRequest;
import com.laundry.dto.response.ConversationResponse;
import com.laundry.entity.Conversation;
import com.laundry.entity.ParticipantInfo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-02T14:48:36+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class ConversationMapperImpl implements ConversationMapper {

    @Override
    public Conversation toEntity(ConversationRequest arg0) {
        if ( arg0 == null ) {
            return null;
        }

        Conversation.ConversationBuilder conversation = Conversation.builder();

        conversation.type( arg0.getType() );

        return conversation.build();
    }

    @Override
    public ConversationResponse toResponse(Conversation arg0) {
        if ( arg0 == null ) {
            return null;
        }

        ConversationResponse.ConversationResponseBuilder conversationResponse = ConversationResponse.builder();

        conversationResponse.id( arg0.getId() );
        conversationResponse.type( arg0.getType() );
        conversationResponse.participantHash( arg0.getParticipantHash() );
        List<ParticipantInfo> list = arg0.getParticipants();
        if ( list != null ) {
            conversationResponse.participants( new ArrayList<ParticipantInfo>( list ) );
        }
        conversationResponse.createDate( arg0.getCreateDate() );
        conversationResponse.modifiedDate( arg0.getModifiedDate() );

        return conversationResponse.build();
    }

    @Override
    public List<ConversationResponse> toResponseList(List<Conversation> arg0) {
        if ( arg0 == null ) {
            return null;
        }

        List<ConversationResponse> list = new ArrayList<ConversationResponse>( arg0.size() );
        for ( Conversation conversation : arg0 ) {
            list.add( toResponse( conversation ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(ConversationRequest arg0, Conversation arg1) {
        if ( arg0 == null ) {
            return;
        }

        arg1.setType( arg0.getType() );
    }
}
