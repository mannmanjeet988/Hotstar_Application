package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
          Subscription subscription = new Subscription();
       // subscriptionEntryDto.getUserId();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();
        subscription.setUser(user);
        user.setSubscription(subscription);

        Integer amountPaid = 0;
        if((subscription.getSubscriptionType().toString()).equals(SubscriptionType.BASIC.toString())){
            amountPaid = 500 + (200 * subscription.getNoOfScreensSubscribed());
        } else if ((subscription.getSubscriptionType().toString()).equals(SubscriptionType.PRO.toString())) {
            amountPaid = 800 + (250 * subscription.getNoOfScreensSubscribed());
        } else {
            amountPaid = 1000 + (350 * subscription.getNoOfScreensSubscribed());
        }
        subscription.setTotalAmountPaid(amountPaid);
       // subscriptionRepository.save(subscription);
        userRepository.save(user);

        return amountPaid;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get();
        Subscription subscription = user.getSubscription();
        int extraAmount= 0;
        if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        }
        if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.BASIC)){
            subscription.setSubscriptionType(SubscriptionType.PRO);
            extraAmount= 300 + (50 * subscription.getNoOfScreensSubscribed());
            //subscriptionRepository.save(subscription);
            userRepository.save(user);
        }
        else if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.PRO)){
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            extraAmount= 200 + (100 * subscription.getNoOfScreensSubscribed());
            //subscriptionRepository.save(subscription);
            userRepository.save(user);      // either save subscription or save user(user is parent),both will work

        }
        return extraAmount;
        //return null;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> subscriptionList = subscriptionRepository.findAll();
       Integer revenue = 0;
       for(int i =0; i< subscriptionList.size();i++)
       {
           revenue +=subscriptionList.get(i).getTotalAmountPaid();
       }
        return revenue;
    }

}
